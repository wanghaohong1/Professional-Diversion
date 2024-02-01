package com.glxy.pro.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.glxy.pro.DTO.PageDTO;
import com.glxy.pro.DTO.UserStudentDTO;
import com.glxy.pro.bo.StudentBo;
import com.glxy.pro.bo.UserBo;
import com.glxy.pro.common.BizException;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.Category;
import com.glxy.pro.entity.Student;
import com.glxy.pro.entity.User;
import com.glxy.pro.query.StudentQuery;
import com.glxy.pro.service.*;
import com.glxy.pro.util.LoginUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.regex.Pattern;

import static com.glxy.pro.common.CommonEnum.*;
import static com.glxy.pro.common.CommonEnum.EMAIL_IS_BIND;
import static com.glxy.pro.constant.CommonConstant.REGEX_EMAIL;
import static com.glxy.pro.constant.CommonConstant.REGEX_PHONE;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@RestController
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private IDivisionResultService divisionResultService;
    @Autowired
    private IGaokaoService gaokaoService;
    @Autowired
    private IFreshmanGradesService freshmanGradesService;
    @Autowired
    private IVolunteerService volunteerService;
    @Autowired
    private ICategoryService categoryService;

    /**
     * 校验手机号和邮箱是否已被绑定
     * @return 校验通过返回null，否则返回错误信息
     */
    private ResultBody checkPhoneAndEmail(String phone, String email) {
        // 校验手机号
        if (StringUtils.isNotBlank(phone) && !Pattern.matches(REGEX_PHONE, phone)) {
            return ResultBody.error(PHONE_IS_NOT_VALID);
        }
        // 校验邮箱
        if (StringUtils.isNotBlank(email) && !Pattern.matches(REGEX_EMAIL, email)) {
            return ResultBody.error(EMAIL_IS_NOT_VALID);
        }
        // 判断手机号是否已被绑定
        if (StringUtils.isNotBlank(phone)) {
            String userId = userService.getUserIdByPhone(phone);
            if (userId != null) {
                ResultBody res = new ResultBody();
                res.setCode(PHONE_IS_BIND.getResultCode());
                res.setData(userId);
                res.setMessage(PHONE_IS_BIND.getResultMsg());
                return res;
            }
        }
        // 判断邮箱是否已被绑定
        if (StringUtils.isNotBlank(email)) {
            String userId = userService.getUserIdByEmail(email);
            if (userId != null)  {
                ResultBody res = new ResultBody();
                res.setCode(EMAIL_IS_BIND.getResultCode());
                res.setData(userId);
                res.setMessage(EMAIL_IS_BIND.getResultMsg());
                return res;
            }
        }
        return null;
    }

    @ApiOperation("修改用户信息")
    @PutMapping("/student/user/updateUser")
    public ResultBody updateUser(@RequestBody UserBo userBo) {
        // 校验参数是否全部为空
        if (StringUtils.isBlank(userBo.getPhone()) && StringUtils.isBlank(userBo.getEmail()) && StringUtils.isBlank(userBo.getPassword())) {
            return ResultBody.error(PARAM_REQUIRE);
        }
        // 获取要修改的用户信息
        User user = userService.getUserById(userBo.getUserId());
        if(user == null) return ResultBody.error(USER_DATA_NOT_EXIST);
        // 空串转null
        if (StringUtils.isBlank(userBo.getPassword())) user.setPassword(null);
        if (StringUtils.isBlank(userBo.getPhone())) user.setPhone(null);
        if (StringUtils.isBlank(userBo.getEmail())) user.setEmail(null);

        // 校验用户名和密码是否一致
        if (user.getUserId().equals(user.getPassword())) {
            return ResultBody.error(USERNAME_PASSWORD_MATCH);
        }else {
            user.setPassword(LoginUtil.encodePassword(userBo.getPassword()));
        }
        // 校验手机号和邮箱是否已被绑定
        ResultBody res = checkPhoneAndEmail(user.getPhone(), user.getEmail());
        if (res != null) {
            String userId = (String) res.getData();
            if (!userId.equals(userBo.getUserId())) return res;
        }
        return userService.updateUser(user) ? ResultBody.success() : ResultBody.error("修改用户失败");
    }

    @ApiOperation("根据手机号获取用户ID")
    @GetMapping("/student/user/getStuIdByPhone")
    public ResultBody getStuIdByPhone(@RequestParam("phone") String phone) {
        String userId = userService.getStudentIdByPhone(phone);
        return userId != null ? ResultBody.success(userId) : ResultBody.error(PHONE_NO_USER);
    }

    @ApiOperation("根据邮箱获取用户ID")
    @GetMapping("/student/user/getStuIdByEmail")
    public ResultBody getStuIdByEmail(@RequestParam("email") String email) {
        String userId = userService.getStudentIdByEmail(email);
        return userId != null ? ResultBody.success(userId) : ResultBody.error(EMAIL_NO_USER);
    }


    // ==================================== 管理员接口 ====================================
    @ApiOperation("批量按学号删除用户所有信息")
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping("teacher/user/cascadingDelete/batch")
    public ResultBody cascadingDelete(@RequestParam("ids") List<String> ids) {
        // 删除用户中的admin，避免教师误删除
        ids.removeIf(element -> "admin".equals(element));
        try {
            userService.removeBatchByIds(ids);
            studentService.removeBatchByIds(ids);
            gaokaoService.removeBatchByIds(ids);
            divisionResultService.removeBatchByIds(ids);

            volunteerService.removeBatchByStuIds(ids);
            freshmanGradesService.removeBatchByStuIds(ids);
            // 删除缓存的信息

            return ResultBody.success();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResultBody.error("删除失败：" + e.getMessage());
        }
    }

    @SaCheckLogin
    @ApiOperation("根据学号查询学生信息")
    @GetMapping("teacher/student/getStudentById/{id}")
    public ResultBody getStudentById(@PathVariable("id") String id) {
        Student student = studentService.getById(id);
        return student == null ? ResultBody.error(DATA_NOT_EXIST) : ResultBody.success(student);
    }

    @SaCheckLogin
    @ApiOperation("修改学生信息")
    @PutMapping("teacher/student/updateStudent")
    public ResultBody updateStudent(@RequestBody StudentBo studentBo) {
        String category = studentBo.getCategory();
        if(category != null) {
            Category categoryByName = categoryService.getCategoryByName(category);
            if(categoryByName == null) return ResultBody.error(CATEGORY_DATA_NOT_EXIST);
            studentBo.setCategoryId(categoryByName.getCategoryId());
        }
        Student student = new Student();
        BeanUtil.copyProperties(studentBo, student);
        boolean result = studentService.updateById(student);
        // 测试一下：如果数据没有改变，那update的结果是true还是false
        return result ? ResultBody.success() : ResultBody.error("修改失败");
    }

    @ApiOperation("根据用户id获取用户信息")
    @GetMapping("teacher/user/getUserInfo/{id}")
    public ResultBody getUserInfo(@PathVariable("id") String id) {
        return ResultBody.success(userService.getUserById(id));
    }

    @SaCheckLogin
    @ApiOperation("分页获取用户管理页面数据")
    @GetMapping("/getUserManagePages")
    public ResultBody getUserManagePages(StudentQuery studentQuery) {
        return ResultBody.success(userService.getUserStudentPage(studentQuery));
    }

    @Transactional(rollbackFor = {Exception.class, BizException.class})
    @SaCheckLogin
    @ApiOperation("添加用户和学生信息")
    @PostMapping("/addUserAndStudent")
    public ResultBody addUserAndStudent(@RequestBody UserStudentDTO userStudentDTO) {
        // 检查唯一性
        ResultBody res = checkPhoneAndEmail(userStudentDTO.getPhone(), userStudentDTO.getEmail());
        if (res != null) return res;

        Student student = new Student();
        User user = new User();
        BeanUtils.copyProperties(userStudentDTO, student);
        BeanUtil.copyProperties(userStudentDTO, user);
        student.setScore(0.);
        user.setUserId(userStudentDTO.getStuId());
        user.setPassword(LoginUtil.encodePassword(user.getPassword()));

        boolean resultOfUser = userService.save(user);
        // 用户添加失败，则不添加学生
        if(!resultOfUser) return ResultBody.error("添加用户失败");

        boolean resultOfStudent = studentService.save(student);
        // 手动回滚
        if(!resultOfStudent) {
            userService.removeById(student.getStuId());
            return ResultBody.error("添加用户失败");
        }
        return ResultBody.success("添加成功");
    }
}

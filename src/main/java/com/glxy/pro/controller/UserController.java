package com.glxy.pro.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.db.nosql.redis.RedisDS;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.glxy.pro.dto.PageDto;
import com.glxy.pro.dto.UserStudentDto;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.regex.Pattern;

import static com.glxy.pro.common.CommonEnum.*;
import static com.glxy.pro.common.CommonEnum.EMAIL_IS_BIND;
import static com.glxy.pro.constant.CommonConstant.*;
import static com.glxy.pro.constant.RedisConstants.TOKEN_CACHE;
import static com.glxy.pro.constant.RedisConstants.USER_CACHE;

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

    @Autowired
    private StringRedisTemplate redisTemplate;

    // ==================================== 学生端接口 ====================================

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
    @PutMapping("student/user/updateUser")
    public ResultBody updateUser(@CookieValue(value = LOGIN_COOKIE, required = false) String token,
                                 @RequestBody UserBo userBo) {
        if (token == null) return ResultBody.error(NEED_LOGIN);
        String stuId = redisTemplate.opsForValue().get(TOKEN_CACHE + token);
        userBo.setId(stuId);
        // 校验参数是否全部为空
        if (StringUtils.isBlank(userBo.getPhone()) && StringUtils.isBlank(userBo.getEmail()) && StringUtils.isBlank(userBo.getPassword())) {
            return ResultBody.error(PARAM_REQUIRE);
        }
        // 获取要修改的用户信息
        User user = userService.getUserById(userBo.getId());
        if(user == null) return ResultBody.error(USER_DATA_NOT_EXIST);
        // 赋值
        if (StringUtils.isNotBlank(userBo.getPassword())) user.setPassword(userBo.getPassword());
        if (StringUtils.isNotBlank(userBo.getPhone())) user.setPhone(userBo.getPhone());
        if (StringUtils.isNotBlank(userBo.getEmail())) user.setEmail(userBo.getEmail());

        // 校验用户名和密码是否一致
        if (user.getId().equals(user.getPassword())) {
            return ResultBody.error(USERNAME_PASSWORD_MATCH);
        }else {
            user.setPassword(LoginUtil.encodePassword(userBo.getPassword()));
        }
        // 校验手机号和邮箱是否已被绑定
        ResultBody res = checkPhoneAndEmail(user.getPhone(), user.getEmail());
        if (res != null) {
            String userId = (String) res.getData();
            if (!userId.equals(userBo.getId())) return res;
        }
        // 修改用户信息 删除用户缓存
        if(userService.updateUser(user)) {
            redisTemplate.delete(USER_CACHE + stuId);
            return ResultBody.success();
        }else {
            return ResultBody.error("修改用户失败");
        }
    }

    @ApiOperation("根据手机号获取用户ID")
    @GetMapping("student/user/getStuIdByPhone")
    public ResultBody getStuIdByPhone(@RequestParam("phone") String phone) {
        String userId = userService.getStudentIdByPhone(phone);
        return userId != null ? ResultBody.success(userId) : ResultBody.error(PHONE_NO_USER);
    }

    @ApiOperation("根据邮箱获取用户ID")
    @GetMapping("student/user/getStuIdByEmail")
    public ResultBody getStuIdByEmail(@RequestParam("email") String email) {
        String userId = userService.getStudentIdByEmail(email);
        return userId != null ? ResultBody.success(userId) : ResultBody.error(EMAIL_NO_USER);
    }

    // ==================================== 管理员接口 ====================================

    @ApiOperation("获取管理员的手机号码和邮箱号码")
    @GetMapping("teacher/user/getAdminInfo")
    public ResultBody getAdminInfo() {
        return ResultBody.success(userService.getAdminEmail());
    }


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
            userService.removeCacheBatch(ids);
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
        return result ? ResultBody.success() : ResultBody.error("修改失败");
    }

    @ApiOperation("管理员修改用户信息")
    @PutMapping("teacher/user/updateUser")
    public ResultBody updateStuUser(@RequestBody UserBo userBo) {
        // 校验参数是否全部为空
        if (StringUtils.isBlank(userBo.getPhone()) && StringUtils.isBlank(userBo.getEmail()) && StringUtils.isBlank(userBo.getPassword())) {
            return ResultBody.error(PARAM_REQUIRE);
        }
        // 获取要修改的用户信息
        User user = userService.getUserById(userBo.getId());
        if(user == null) return ResultBody.error(USER_DATA_NOT_EXIST);
        // 空串转null
        if (StringUtils.isNotBlank(userBo.getPassword())) {
//            user.setPassword(userBo.getPassword());
            if(user.getPassword().equals(userBo.getPassword())) {
                user.setPassword(userBo.getPassword());
            }else {
                user.setPassword(LoginUtil.encodePassword(userBo.getPassword()));
            }
        }
        if (StringUtils.isNotBlank(userBo.getPhone())) user.setPhone(userBo.getPhone());
        if (StringUtils.isNotBlank(userBo.getEmail())) user.setEmail(userBo.getEmail());

        // 校验手机号和邮箱是否已被绑定
        ResultBody res = checkPhoneAndEmail(user.getPhone(), user.getEmail());
        if (res != null) {
            String userId = (String) res.getData();
            if (!userId.equals(userBo.getId())) return res;
        }
        // 修改用户信息 删除用户缓存
        if(userService.updateUser(user)) {
            redisTemplate.delete(USER_CACHE + user.getId());
            return ResultBody.success();
        }else {
            return ResultBody.error("修改用户失败");
        }
    }

    @ApiOperation("根据用户id获取用户信息")
    @GetMapping("teacher/user/getUserInfo/{id}")
    public ResultBody getUserInfo(@PathVariable("id") String id) {
        return ResultBody.success(userService.getUserById(id));
    }

    @SaCheckLogin
    @ApiOperation("分页获取用户管理页面数据")
    @GetMapping("teacher/getUserManagePages")
    public ResultBody getUserManagePages(StudentQuery studentQuery) {
        return ResultBody.success(userService.getUserStudentPage(studentQuery));
    }

    @SaCheckLogin
    @ApiOperation("添加用户和学生信息")
    @Transactional(rollbackFor = {Exception.class, BizException.class})
    @PostMapping("teacher/addUserAndStudent")
    public ResultBody addUserAndStudent(@RequestBody UserStudentDto userStudentDto) {
        // 检查唯一性
        ResultBody res = checkPhoneAndEmail(userStudentDto.getPhone(), userStudentDto.getEmail());
        if (res != null) return res;

        Student student = new Student();
        User user = new User();
        BeanUtils.copyProperties(userStudentDto, student);
        BeanUtil.copyProperties(userStudentDto, user);
        student.setScore(0.);
        user.setId(userStudentDto.getStuId());
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

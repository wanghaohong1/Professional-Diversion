package com.glxy.pro.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.glxy.pro.bo.UserBo;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.User;
import com.glxy.pro.service.IStudentService;
import com.glxy.pro.service.IUserService;
import com.glxy.pro.util.LoginUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

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
@RequestMapping
public class UserController {
    @Autowired
    private IUserService userService;

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
}

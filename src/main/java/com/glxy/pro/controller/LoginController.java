package com.glxy.pro.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.apistd.uni.Uni;
import com.apistd.uni.UniException;
import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniMessage;
import com.apistd.uni.sms.UniSMS;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.glxy.pro.DTO.LoginDTO;
import com.glxy.pro.bo.EmailBo;
import com.glxy.pro.bo.UserBo;
import com.glxy.pro.common.CommonEnum;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.constant.RedisConstants;
import com.glxy.pro.entity.Category;
import com.glxy.pro.entity.Student;
import com.glxy.pro.entity.User;
import com.glxy.pro.service.ICategoryService;
import com.glxy.pro.service.IEmailService;
import com.glxy.pro.service.IStudentService;
import com.glxy.pro.service.IUserService;
import com.glxy.pro.util.LoginUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.alibaba.fastjson2.JSON.parseObject;
import static com.alibaba.fastjson2.JSON.toJSONString;
import static com.glxy.pro.common.CommonEnum.*;
import static com.glxy.pro.constant.CommonConstant.REGEX_EMAIL;
import static com.glxy.pro.constant.CommonConstant.REGEX_PHONE;
import static com.glxy.pro.constant.RedisConstants.*;

@Slf4j
@RestController
@RequestMapping("/student")
public class LoginController {
    @Value("${find-password.by-mail.subject}")
    private String mailSubject;
    @Value("${uni-sms.access-key-id}")
    private String access_key_id;
    @Value("${uni-sms.signature}")
    private String signature;
    @Value("${uni-sms.template-id}")
    private String templateId;

    @Autowired
    private IUserService userService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IEmailService emailService;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @ApiOperation("获取当前登录用户")
    @GetMapping("/getPresentUser")
    public ResultBody getPresentUser() {
        // 1.获取token信息
        String tokenValue = StpUtil.getTokenInfo().getTokenValue();
        // 2.根据token从缓存中取出登录的用户
        String userId = redisTemplate.opsForValue().get(RedisConstants.TOKEN_CACHE + tokenValue);
        // 3.判断用户登录状态
        if (StringUtils.isBlank(userId)) return ResultBody.error(CommonEnum.USER_NO_LOGIN);
        try {
            int result = Integer.parseInt(userId);
            if (result < 0) return ResultBody.error(CommonEnum.USER_GET_OUT);
        } catch (NumberFormatException e) {
            // 报错说明token中有用户信息，没有被挤掉，捕获但不对异常进行处理
        }
        // 4.已登录，获取用户信息 为空直接返回
        User byId = userService.getUserById(userId);
        if (byId == null) return ResultBody.error(USER_DATA_NOT_EXIST);
        UserBo userBo = new UserBo();
        BeanUtils.copyProperties(byId, userBo);
        userBo.setRemember(!StringUtils.isBlank(redisTemplate.opsForValue().get(USER_REMEMBER_CACHE + userId)));
        // 如果是管理员，直接返回
        if (Objects.equals(userBo.getUserId(), "admin")) {
            return ResultBody.success(userBo);
        }
        // 获取学生信息
        LoginDTO loginVo = new LoginDTO();
        Student presentStudent = studentService.getById(userBo.getUserId());
        BeanUtils.copyProperties(presentStudent, loginVo);
        BeanUtils.copyProperties(userBo, loginVo);
        loginVo.setSexString(loginVo.getSex() == 0 ? "男" : "女");
        // 获取学生大类信息
        Category category = categoryService.getById(loginVo.getCategoryId());
        loginVo.setCategory(category.getCategoryName());
        return ResultBody.success(loginVo);
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResultBody login(@RequestBody UserBo loginMsg) {
        //1. 判断是否已经登录
        if (StpUtil.isLogin()) return ResultBody.success();
        //2. 判断 bo 是否为空
        if (loginMsg == null) return ResultBody.error(PARAM_REQUIRE);
        //3. 检查账密是否正确
        loginMsg.setPassword(LoginUtil.encodePassword(loginMsg.getPassword()));
        if (!userService.checkLogin(loginMsg)) return ResultBody.error(CommonEnum.USERNAME_PASSWORD_ERROR);
        // MySQL忽略大小写，数据库中均为小写，用户登录也需要将小写的用户id作为登录凭证
        String userId = loginMsg.getUserId().toLowerCase();
        StpUtil.login(userId, loginMsg.isRemember());
        if (StpUtil.isLogin()) {
            //4. 如果登录成功，返回token给前端
            if (loginMsg.isRemember()) {
                redisTemplate.opsForValue().set(USER_REMEMBER_CACHE + userId, "1");
                redisTemplate.expire(USER_REMEMBER_CACHE + userId, SEVEN_DAY_TTL, TimeUnit.SECONDS);
            }
            String tokenValue = StpUtil.getTokenInfo().getTokenValue();
            return ResultBody.success(tokenValue);
        } else {
            return ResultBody.error("登录失败");
        }
    }

    @SaCheckLogin
    @ApiOperation("用户登出")
    @GetMapping("/logout")
    public ResultBody logout() {
        UserBo userBo = parseObject(toJSONString(getPresentUser().getData()), UserBo.class);
        redisTemplate.delete(USER_REMEMBER_CACHE + userBo.getUserId());
        StpUtil.logout();
        return ResultBody.success();
    }

    @ApiOperation("发送验证码")
    @PostMapping("/sendVerification")
    public ResultBody sendVerification(@RequestBody UserBo userBo) throws Exception {
        boolean res = StringUtils.isNotBlank(userBo.getPhone()) && (userBo.getPhone() != null);
        return res ? sms(userBo.getPhone()) : email(userBo.getEmail());
    }

    /**
     * 发送手机验证码
     */
    private ResultBody sms(String phone) {
        // 判断手机号是否为空
        if (StringUtils.isBlank(phone)) {
            return ResultBody.error(PARAM_REQUIRE);
        }
        // 判断手机号是否合法
        if (!Pattern.matches(REGEX_PHONE, phone)) {
            return ResultBody.error(PHONE_IS_NOT_VALID);
        }
        // 判断手机号是否已被绑定
        String userId = userService.getUserIdByPhone(phone);
        if(StringUtils.isBlank(userId)) {
            return ResultBody.error(USER_DATA_NOT_EXIST);
        }
        // 初始化
        Uni.init(access_key_id);
        // 生成5分钟验证码并存入缓存
        String verification = LoginUtil.getVerification(userId, redisTemplate);
        String ttl = String.valueOf(RedisConstants.VERIFICATION_FIVE_MIN_TTL / 60);
        // 设置自定义参数
        Map<String, String> templateData = new HashMap<>();
        templateData.put("code", verification);
        templateData.put("ttl", ttl);
        // 构建信息
        UniMessage message = UniSMS.buildMessage()
                .setTo(phone)
                .setSignature(signature)
                .setTemplateId(templateId)
                .setTemplateData(templateData);
        // 发送短信
        try {
            UniResponse res = message.send();
            return ResultBody.success(res.message);
        } catch (UniException e) {
            log.error(e.toString());
            return ResultBody.error("短信发送失败，请重试");
        }
    }

    /**
     * 发送邮箱验证码
     */
    private ResultBody email(String email) throws Exception {
        // 判断邮箱是否为空
        if (StringUtils.isBlank(email)) {
            return ResultBody.error(PARAM_REQUIRE);
        }
        // 判断邮箱是否合法
        if (!Pattern.matches(REGEX_EMAIL, email)) {
            return ResultBody.error(EMAIL_IS_NOT_VALID);
        }
        // 判断邮箱号是否已被绑定
        String userId = userService.getUserIdByEmail(email);
        if(StringUtils.isBlank(userId)) {
            return ResultBody.error(USER_DATA_NOT_EXIST);
        }
        // 生成验证码，并存入redis
        String verification = LoginUtil.getVerification(userId, redisTemplate);
        // 发送邮件
        User admin = userService.getById("admin");
        EmailBo e = new EmailBo();
        e.setPhone(admin.getPhone());
        e.setVerification(verification);
        String emailContent = LoginUtil.wrappingTemplate("emailBo", "email", e, freeMarkerConfigurer);
        emailService.sendHtmlMail(email, mailSubject, emailContent);
        return ResultBody.success(verification);
    }

    @ApiOperation("校验验证码")
    @GetMapping("/checkFindPasswordVerification/{verification}/{userId}")
    public ResultBody checkFindPasswordVerification(@PathVariable("userId") String userId, @PathVariable("verification") String verification) {
        //1.获取该用户对应的验证码
        String result = redisTemplate.opsForValue().get(VERIFICATION_CACHE + userId);
        if (StringUtils.isBlank(result)) {
            return ResultBody.error("验证码已过期");
        }
        //2.校验验证码是否一致
        if (result.equals(verification)) {
            return ResultBody.success(userService.getUserById(userId));
        }
        return ResultBody.error("验证码错误");
    }

}
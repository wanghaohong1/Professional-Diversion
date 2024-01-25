package com.glxy.pro.util;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.glxy.pro.bo.EmailBo;
import com.glxy.pro.constant.CommonConstant;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.glxy.pro.constant.RedisConstants.VERIFICATION_CACHE;
import static com.glxy.pro.constant.RedisConstants.VERIFICATION_FIVE_MIN_TTL;

/**
 * @author Alonha
 * @create 2023-10-12-19:53
 */
@Component
public class LoginUtil {
    private static final String salt;

    static {
        salt = CommonConstant.salt;
    }

    /**
     * 密码加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    public static String encodePassword(String password) {
        if (password == null || password.length() == 0) {
            return null;
        }
        // 使用Hutool工具类，创建md5摘要器
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        // 添加佐料
        md5.setSalt(salt.getBytes());
        return md5.digestHex(password);
    }

    /**
     * 生成验证码
     *
     * @param key 用户id
     * @return 验证码
     */
    public static String getVerification(String key, StringRedisTemplate redisTemplate) {
        // 从redis中获取该学生曾经获取的验证码
        String oldVerification = redisTemplate.opsForValue().get(VERIFICATION_CACHE + key);
        // 判断验证码不为空且为数字字符串就返回
        if (StrUtil.isNotBlank(oldVerification) && StrUtil.isNumeric(oldVerification)) {
            return oldVerification;
        }
        // 获取验证码
        String verification = RandomUtil.randomNumbers(6);
        // 缓存验证码到redis中，设置过期时间为5分钟
        redisTemplate.opsForValue()
                .set(VERIFICATION_CACHE + key, verification, VERIFICATION_FIVE_MIN_TTL, TimeUnit.SECONDS);
        return verification;
    }

    /**
     * 将模板文件及数据渲染完成之后，转换为html字符串
     *
     * @param templateName         模板名称
     * @param fileName             模板文件名
     * @param emailBo              邮件对象
     * @param freeMarkerConfigurer freemarker配置类
     * @return html字符串
     */
    public static String wrappingTemplate(String templateName, String fileName, EmailBo emailBo, FreeMarkerConfigurer freeMarkerConfigurer) throws IOException, TemplateException {
        //添加动态数据，替换模板里面的占位符
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate(fileName + ".html");
        //将模板文件及数据渲染完成之后，转换为html字符串
        Map<String, Object> model = new HashMap<>();
        model.put(templateName, emailBo);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }
}

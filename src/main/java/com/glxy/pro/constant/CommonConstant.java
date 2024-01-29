package com.glxy.pro.constant;

import java.time.LocalDate;

/**
 * 公共常量类
 */
public class CommonConstant {
    /**
     * 当前年级
     */
    public static final Integer CURRENT_GRADE = LocalDate.now().getYear() - 1;

    /**
     * 当前年份
     */
    public static final Integer CURRENT_YEAR = LocalDate.now().getYear();

    /**
     * 手机号正则表达式
     */
    public static final String REGEX_PHONE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(3|1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";

    /**
     * 电子邮箱正则表达式
     */
    public static final String REGEX_EMAIL = "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$";

    /**
     * 用户角色
     */
    public static final String TEACHER = "teacher";
    public static final String STUDENT = "student";

    /**
     * 性别 - 0男 1女
     */
    public static final Integer man = 0;
    public static final Integer woman = 1;

    /**
     * 文理分科 - 0理 1文
     */
    public static final Integer science = 0;
    public static final Integer humanities = 1;

    /**
     * 密码加密佐料
     */
    public static final String salt = "https://www.gdut.edu.cn/";

    /**
     * 登录Cookie
     */
    public static final String LOGIN_COOKIE = "satoken";

}

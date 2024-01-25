package com.glxy.pro.DTO;

import lombok.Data;

@Data
public class LoginDTO {
    /**
     * 学号
     */
    private String id;

    /**
     * 登录token
     */
    private String token;

    /**
     * 记住我（七天免登录）
     */
    private boolean remember;

    /**
     * 密码
     */
    private String password;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 性别字符串
     */
    private String sexString;

    /**
     * 年级
     */
    private String grade;

    /**
     * 所属大类id
     */
    private String categoryId;

    /**
     * 所属大类名称
     */
    private String category;

    /**
     * 学生班级
     */
    private String stuClass;

    /**
     * 总绩点
     */
    private Double score;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

}

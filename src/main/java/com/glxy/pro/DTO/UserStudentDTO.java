package com.glxy.pro.dto;

import lombok.Data;

@Data
public class UserStudentDto {
    /**
     * 学号
     */
    private String stuId;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 性别的字符串标标识
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
     * 密码
     */
    private String password;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;
}

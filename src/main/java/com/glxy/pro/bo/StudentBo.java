package com.glxy.pro.bo;

import lombok.Data;

@Data
public class StudentBo {
    /**
     * 学号
     */
    private String stuId;

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
     * 性别的字符串标标识
     */
    private String sexString;

    /**
     * 年级
     */
    private String grade;

    /**
     * 所属大类名称
     */
    private String category;

    /**
     * 所属大类id
     */
    private String categoryId;


    /**
     * 学生班级
     */
    private String stuClass;

    /**
     * 总绩点
     */
    private Double score;

}


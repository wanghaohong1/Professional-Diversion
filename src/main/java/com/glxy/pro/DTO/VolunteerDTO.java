package com.glxy.pro.dto;

import lombok.Data;

/**
 * @author Alonha
 * @create 2023-12-02-15:23
 */
@Data
public class VolunteerDto {

    /**
     * 学号
     */
    private String stuId;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 年级
     */
    private String grade;

    /**
     * 所属大类名称
     */
    private String category;

    /**
     * 学生班级
     */
    private String stuClass;

    /**
     * 第几志愿
     */
    private Integer which;

    /**
     * 专业名称
     */
    private String majorName;

}

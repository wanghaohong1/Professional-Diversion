package com.glxy.pro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@TableName("freshman_grades")
@Data
public class FreshmanGrades implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    @TableField("stu_id")
    private String stuId;

    /**
     * 课程名称
     */
    @TableField("course_name")
    private String courseName;

    /**
     * 课程学分
     */
    @TableField("course_weight")
    private Double courseWeight;

    /**
     * 单科绩点
     */
    @TableField("course_point")
    private Double coursePoint;

    /**
     * 单科成绩
     */
    @TableField("course_score")
    private Double courseScore;
}

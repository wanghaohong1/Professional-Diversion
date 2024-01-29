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
@Data
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    @TableId("stu_id")
    private String stuId;

    /**
     * 学生姓名
     */
    @TableField("stu_name")
    private String name;

    /**
     * 性别:0——男，1——女
     */
    @TableField("stu_sex")
    private Integer sex;

    /**
     * 年级
     */
    @TableField("stu_grade")
    private String grade;

    /**
     * 所属大类ID
     */
    @TableField("category_id")
    private String categoryId;

    /**
     * 班级
     */
    @TableField("stu_class")
    private String stuClass;

    /**
     * 总绩点
     */
    @TableField("stu_score")
    private Double score;
}

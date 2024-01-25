package com.glxy.pro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
public class Volunteer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    @TableField("stu_id")
    private String stuId;

    /**
     * 专业ID
     */
    @TableField("major_id")
    private String majorId;

    /**
     * 第几志愿
     */
    private Integer which;

}

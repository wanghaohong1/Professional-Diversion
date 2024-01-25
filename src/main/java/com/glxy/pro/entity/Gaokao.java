package com.glxy.pro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Gaokao implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    @TableId("stu_id")
    private String stuId;

    /**
     * 高考录取类型
     */
    @TableField("stu_from")
    private String stuFrom;

    /**
     * 文理分科:0——理，1——文
     */
    @TableField("sci_lib")
    private Integer sciLib;

    /**
     * 分数线
     */
    @TableField("score_line")
    private Double scoreLine;

    /**
     * 高考总分
     */
    @TableField("gk_score")
    private Double gkScore;

}

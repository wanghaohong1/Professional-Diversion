package com.glxy.pro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
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
@TableName("division_result")
public class DivisionResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    @TableId("stu_id")
    private String stuId;

    /**
     * 专业ID
     */
    @TableField("major_id")
    private String majorId;

    /**
     * 排名
     */
    @TableField("ranking")
    private Integer ranking;

    /**
     * 高考折算成绩
     */
    @TableField("gaokao_final_score")
    private Double gaokaoFinalScore;

    /**
     * 大一折算成绩
     */
    @TableField("freshman_final_score")
    private Double freshmanFinalScore;

    /**
     * 综合成绩
     */
    @TableField("final_score")
    private Double finalScore;

}

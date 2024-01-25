package com.glxy.pro.DTO;

import com.glxy.pro.entity.FreshmanGrades;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GradeListDTO {
    /**
     * 高考文理分科:0——理，1——文
     */
    @ApiModelProperty("高考文理分科")
    private Integer sciLib;

    /**
     * 高考分数线
     */
    @ApiModelProperty("高考分数线")
    private Double scoreLine;

    /**
     * 高考总分
     */
    @ApiModelProperty("高考总分")
    private Double gkScore;

    /**
     * 大一成绩列表
     */
    @ApiModelProperty("大一成绩列表")
    private List<FreshmanGrades> freshmanGradesList;

    /**
     * 高考折算成绩
     */
    @ApiModelProperty("高考折算成绩")
    private Double gaokaoFinalScore;

    /**
     * 大一折算成绩
     */
    @ApiModelProperty("大一折算成绩")
    private Double freshmanFinalScore;

    /**
     * 综合成绩
     */
    @ApiModelProperty("综合成绩")
    private Double finalScore;

    /**
     * 排名
     */
    @ApiModelProperty("排名")
    private Integer ranking;

    /**
     * 录取批次
     */
    @ApiModelProperty("录取批次")
    private String stuFrom;

}

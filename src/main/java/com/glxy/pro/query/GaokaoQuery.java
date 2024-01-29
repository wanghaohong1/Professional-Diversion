package com.glxy.pro.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GaokaoQuery extends PageQuery {
    /**
     * 学生ID列表
     */
    @ApiModelProperty("学生ID列表")
    private List<String> stuIds;


    /**
     * 文理分科:0——理，1——文
     */
    @ApiModelProperty("文理分科")
    private Integer sciLib;

    /**
     * 生源地
     */
    @ApiModelProperty("生源地")
    private String stuFrom;


    /**
     * 分数线
     */
    @ApiModelProperty("分数线")
    private Double scoreLine;

    /**
     * 高考总分
     */
    @ApiModelProperty("高考总分")
    private Double gkScore;

}

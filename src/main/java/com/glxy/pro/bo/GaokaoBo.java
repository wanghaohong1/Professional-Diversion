package com.glxy.pro.bo;

//import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 高考信息
 */
@Data
public class GaokaoBo {
    /**
     * 学号
     */
//    @ApiModelProperty("学号")
    private String stuId;

    /**
     * 录取批次
     */
//    @ApiModelProperty("录取批次")
    private String stuFrom;

    /**
     * 文理分科:0——理，1——文
     */
//    @ApiModelProperty("文理分科")
    private Integer sciLib;

    /**
     * 分数线
     */
//    @ApiModelProperty("分数线")
    private Double scoreLine;

    /**
     * 高考总分
     */
//    @ApiModelProperty("高考总分")
    private Double gkScore;

}

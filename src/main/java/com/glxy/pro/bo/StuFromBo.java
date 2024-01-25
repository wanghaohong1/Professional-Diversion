package com.glxy.pro.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StuFromBo {
    @ApiModelProperty("录取年份")
    private String grade;

    @ApiModelProperty("高考录取类型")
    private String stuFrom;

    @ApiModelProperty("录取分数线")
    private Double scoreLine;

    @ApiModelProperty("人数")
    private Integer stuNum;
}

package com.glxy.pro.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DivisionResultBo {
    /**
     * 学号
     */
    @ApiModelProperty("学号")
    private String stuId;

    /**
     * 学生姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 专业ID
     */
    @ApiModelProperty("专业ID")
    private String majorId;

    /**
     * 专业名称
     */
    @ApiModelProperty("专业名称")
    private String majorName;

    /**
     * 排名
     */
    @ApiModelProperty("排名")
    private Integer ranking;

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
     * 文理分科:0——理，1——文
     */
    @ApiModelProperty("文理分科")
    private Integer sciLib;

}

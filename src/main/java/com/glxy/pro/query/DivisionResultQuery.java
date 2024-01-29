package com.glxy.pro.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("录取结果查询条件实体")
public class DivisionResultQuery extends PageQuery{
    /**
     * 学号
     */
    @ApiModelProperty("学号")
    private String stuId;

    /**
     * 大类ID
     */
    @ApiModelProperty("大类ID")
    private String categoryId;

    /**
     * 学生姓名
     */
    @ApiModelProperty("学生姓名")
    private String name;

    /**
     * 专业ID
     */
    @ApiModelProperty("专业ID数组")
    private List<String> majorIds;

    /**
     * 排名
     */
    @ApiModelProperty("排名")
    private Integer ranking;

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

    /**
     * 年级
     */
    @ApiModelProperty("学生年级")
    private String grade;

}

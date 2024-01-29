package com.glxy.pro.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("大一成绩查询实体")
public class FreshmanGradesQuery extends PageQuery {
    /**
     * 学号
     */
    @ApiModelProperty("学生ID列表")
    private List<String> stuIds;

    /**
     * 课程名称
     */
    @ApiModelProperty("课程名称")
    private String courseName;

    /**
     * 课程学分
     */
    @ApiModelProperty("课程学分")
    private Double courseWeight;

    /**
     * 单科绩点
     */
    @ApiModelProperty("单科绩点")
    private Double coursePoint;

    /**
     * 单科成绩
     */
    @ApiModelProperty("单科成绩")
    private Double courseScore;
}
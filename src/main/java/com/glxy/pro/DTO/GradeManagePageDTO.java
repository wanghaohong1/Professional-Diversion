package com.glxy.pro.dto;

import com.glxy.pro.entity.FreshmanGrades;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class GradeManagePageDto {
    /**
     * 学号
     */
    @ApiModelProperty("学号")
    private String stuId;

    /**
     * 学生姓名
     */
    @ApiModelProperty("学生姓名")
    private String name;

    /**
     * 年级
     */
    @ApiModelProperty("年级")
    private String grade;

    /**
     * 所属大类id
     */
    @ApiModelProperty("所属大类id")
    private String categoryId;

    /**
     * 所属大类名称
     */
    @ApiModelProperty("所属大类名称")
    private String category;

    /**
     * 科类
     */
    @ApiModelProperty("科类")
    private Integer sciLib;

    /**
     * 高考文理分科:0——理，1——文
     */
    @ApiModelProperty("高考文理分科")
    private String sciLibStr;

    /**
     * 绩点
     */
    @ApiModelProperty("绩点")
    private Double score;

    /**
     * 高考成绩
     */
    @ApiModelProperty("高考成绩")
    private Double gkScore;

    /**
     * 高考录取类别
     */
    @ApiModelProperty("高考录取类别")
    private String stuFrom;

    /**
     * 高考录取批次线
     */
    @ApiModelProperty("高考录取批次线")
    private Double scoreLine;

    /**
     * 综合成绩
     */
    @ApiModelProperty("综合成绩")
    private Double finalScore;

    /**
     * 大一成绩列表
     */
    @ApiModelProperty("大一成绩列表")
    private List<FreshmanGrades> freshmanGradesList;
}

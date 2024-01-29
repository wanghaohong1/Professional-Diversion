package com.glxy.pro.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Alonha
 * @create 2023-10-16-0:08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StudentQuery extends PageQuery {

    /**
     * 学生ID列表
     */
    @ApiModelProperty("学生ID列表")
    private List<String> stuIds;

    /**
     * 学生姓名
     */
    @ApiModelProperty("学生姓名")
    private String name;

    /**
     * 性别:0——男，1——女
     */
    @ApiModelProperty("学生性别")
    private Integer sex;

    /**
     * 年级
     */
    @ApiModelProperty("学生年级")
    private String grade;

    /**
     * 所属大类ID
     */
    @ApiModelProperty("大类id")
    private String categoryId;

    /**
     * 班级
     */
    @ApiModelProperty("班级")
    private String stuClass;

    /**
     * 科类
     */
    @ApiModelProperty("科类")
    private Integer sciLib;

    /**
     * 总绩点
     */
    @ApiModelProperty("总绩点")
    private Double score;

    /**
     * 专业名称
     */
    @ApiModelProperty("专业名称")
    private String majorName;
}

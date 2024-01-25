package com.glxy.pro.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Alonha
 * @create 2023-10-20-22:02
 */
@Data
public class CategoryBo {
    /**
     * 大类ID
     */
    @ApiModelProperty("大类ID")
    private String categoryId;

    /**
     * 大类名称
     */
    @ApiModelProperty("大类名称")
    private String categoryName;

    /**
     * 学生总数
     */
    @ApiModelProperty("学生总数")
    private Integer stuNum;
}

package com.glxy.pro.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MajorBo {
    /**
     * 专业ID
     */
    @ApiModelProperty("专业ID")
    private String majorId;

    /**
     * 所属大类ID
     */
    @ApiModelProperty("所属大类ID")
    private String categoryId;

    /**
     * 专业名称
     */
    @ApiModelProperty("专业名称")
    private String majorName;
}

package com.glxy.pro.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VolunteerBo {

    @ApiModelProperty("学号")
    private String stuId;

    @ApiModelProperty("专业ID")
    private String majorId;

    @ApiModelProperty("第几志愿")
    private Integer which;
}

package com.glxy.pro.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AutoAdmissionBo {

    @ApiModelProperty("大类ID")
    private String categoryId;

    @ApiModelProperty("学号列表")
    private List<String> studentIds;

    @ApiModelProperty("学生志愿列表")
    private List<VolunteerBo> volunteerBoList;
}

package com.glxy.pro.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VolunteerQuery extends PageQuery{

    @ApiModelProperty("学号列表")
    private List<String> stuIds;

    @ApiModelProperty("大类ID")
    private String categoryId;

}

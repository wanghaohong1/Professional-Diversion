package com.glxy.pro.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("分流情况查询实体")
public class AdmissionQuery extends PageQuery {
    /**
     * 专业ID
     */
    @ApiModelProperty("专业ID数组")
    private List<String> majorIds;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private Integer admYear;

}

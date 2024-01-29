package com.glxy.pro.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AdmissionBo {

    @ApiModelProperty("专业ID")
    private String majorId;

    @ApiModelProperty("专业名称")
    private String majorName;

    @ApiModelProperty("大类名称")
    private String categoryName;

    @ApiModelProperty("年份")
    private Integer admYear;

    @ApiModelProperty("文科招生人数")
    private Integer humanitiesStuCount;

    @ApiModelProperty("理科招生人数")
    private Integer scienceStuCount;

    @ApiModelProperty("文科最低排名")
    private Integer humanitiesLow;

    @ApiModelProperty("文科最高排名")
    private Integer humanitiesHigh;

    @ApiModelProperty("理科最低排名")
    private Integer scienceLow;

    @ApiModelProperty("理科最高排名")
    private Integer scienceHigh;

    @ApiModelProperty("当前文科录取人数")
    private Integer nowHumanitiesStuCount;

    @ApiModelProperty("当前理科录取人数")
    private Integer nowScienceStuCount;
}

package com.glxy.pro.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AdmissionBo {
    /**
     * 专业ID
     */
    @ApiModelProperty("专业ID")
    private String majorId;

    /**
     * 专业名称
     */
    @ApiModelProperty("专业名称")
    private String majorName;

    /**
     * 大类名称
     */
    @ApiModelProperty("大类名称")
    private String categoryName;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private Integer admYear;

    /**
     * 文科招生人数
     */
    @ApiModelProperty("文科招生人数")
    private Integer humanitiesStuCount;

    /**
     * 理科招生人数
     */
    @ApiModelProperty("理科招生人数")
    private Integer scienceStuCount;

    /**
     * 文科最低排名
     */
    @ApiModelProperty("文科最低排名")
    private Integer humanitiesLow;

    /**
     * 文科最高排名
     */
    @ApiModelProperty("文科最高排名")
    private Integer humanitiesHigh;

    /**
     * 理科最低排名
     */
    @ApiModelProperty("理科最低排名")
    private Integer scienceLow;

    /**
     * 理科最高排名
     */
    @ApiModelProperty("理科最高排名")
    private Integer scienceHigh;

    @ApiModelProperty("当前文科录取人数")
    private Integer nowHumanitiesStuCount;

    @ApiModelProperty("当前理科录取人数")
    private Integer nowScienceStuCount;
}

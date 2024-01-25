package com.glxy.pro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Data
public class Admission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专业ID
     */
    @TableField("major_id")
    private String majorId;

    /**
     * 年份
     */
    @TableField("adm_year")
    private Integer admYear;

    /**
     * 文科招生人数
     */
    @TableField("humanities_stu_count")
    private Integer humanitiesStuCount;

    /**
     * 理科招生人数
     */
    @TableField("science_stu_count")
    private Integer scienceStuCount;

    /**
     * 文科最低排名
     */
    @TableField("humanities_low")
    private Integer humanitiesLow;

    /**
     * 文科最高排名
     */
    @TableField("humanities_high")
    private Integer humanitiesHigh;

    /**
     * 理科最低排名
     */
    @TableField("science_low")
    private Integer scienceLow;

    /**
     * 理科最高排名
     */
    @TableField("science_high")
    private Integer scienceHigh;

    /**
     * 当前文科录取人数
     */
    @TableField("now_humanities_stu_count")
    private Integer nowHumanitiesStuCount;

    /**
     * 当前理科录取人数
     */
    @TableField("now_science_stu_count")
    private Integer nowScienceStuCount;

}

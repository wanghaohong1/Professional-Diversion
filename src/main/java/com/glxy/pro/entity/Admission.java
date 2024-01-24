package com.glxy.pro.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
public class Admission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专业ID
     */
    private String majorId;

    /**
     * 年份
     */
    private Integer admYear;

    /**
     * 文科招生人数
     */
    private Integer humanitiesStuCount;

    /**
     * 理科招生人数
     */
    private Integer scienceStuCount;

    /**
     * 文科最低排名
     */
    private Integer humanitiesLow;

    /**
     * 文科最高排名
     */
    private Integer humanitiesHigh;

    /**
     * 理科最低排名
     */
    private Integer scienceLow;

    /**
     * 理科最高排名
     */
    private Integer scienceHigh;

    /**
     * 当前文科录取人数
     */
    private Integer nowHumanitiesStuCount;

    /**
     * 当前理科录取人数
     */
    private Integer nowScienceStuCount;

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public Integer getAdmYear() {
        return admYear;
    }

    public void setAdmYear(Integer admYear) {
        this.admYear = admYear;
    }

    public Integer getHumanitiesStuCount() {
        return humanitiesStuCount;
    }

    public void setHumanitiesStuCount(Integer humanitiesStuCount) {
        this.humanitiesStuCount = humanitiesStuCount;
    }

    public Integer getScienceStuCount() {
        return scienceStuCount;
    }

    public void setScienceStuCount(Integer scienceStuCount) {
        this.scienceStuCount = scienceStuCount;
    }

    public Integer getHumanitiesLow() {
        return humanitiesLow;
    }

    public void setHumanitiesLow(Integer humanitiesLow) {
        this.humanitiesLow = humanitiesLow;
    }

    public Integer getHumanitiesHigh() {
        return humanitiesHigh;
    }

    public void setHumanitiesHigh(Integer humanitiesHigh) {
        this.humanitiesHigh = humanitiesHigh;
    }

    public Integer getScienceLow() {
        return scienceLow;
    }

    public void setScienceLow(Integer scienceLow) {
        this.scienceLow = scienceLow;
    }

    public Integer getScienceHigh() {
        return scienceHigh;
    }

    public void setScienceHigh(Integer scienceHigh) {
        this.scienceHigh = scienceHigh;
    }

    public Integer getNowHumanitiesStuCount() {
        return nowHumanitiesStuCount;
    }

    public void setNowHumanitiesStuCount(Integer nowHumanitiesStuCount) {
        this.nowHumanitiesStuCount = nowHumanitiesStuCount;
    }

    public Integer getNowScienceStuCount() {
        return nowScienceStuCount;
    }

    public void setNowScienceStuCount(Integer nowScienceStuCount) {
        this.nowScienceStuCount = nowScienceStuCount;
    }

    @Override
    public String toString() {
        return "Admission{" +
            "majorId = " + majorId +
            ", admYear = " + admYear +
            ", humanitiesStuCount = " + humanitiesStuCount +
            ", scienceStuCount = " + scienceStuCount +
            ", humanitiesLow = " + humanitiesLow +
            ", humanitiesHigh = " + humanitiesHigh +
            ", scienceLow = " + scienceLow +
            ", scienceHigh = " + scienceHigh +
            ", nowHumanitiesStuCount = " + nowHumanitiesStuCount +
            ", nowScienceStuCount = " + nowScienceStuCount +
        "}";
    }
}

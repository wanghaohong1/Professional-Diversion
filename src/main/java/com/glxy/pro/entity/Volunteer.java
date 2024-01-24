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
public class Volunteer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    private String stuId;

    /**
     * 专业ID
     */
    private String majorId;

    /**
     * 第几志愿
     */
    private Integer which;

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public Integer getWhich() {
        return which;
    }

    public void setWhich(Integer which) {
        this.which = which;
    }

    @Override
    public String toString() {
        return "Volunteer{" +
            "stuId = " + stuId +
            ", majorId = " + majorId +
            ", which = " + which +
        "}";
    }
}

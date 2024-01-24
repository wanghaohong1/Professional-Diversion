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
public class Major implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专业ID
     */
    private String majorId;

    /**
     * 专业名称
     */
    private String majorName;

    /**
     * 所属大类ID
     */
    private String categoryId;

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Major{" +
            "majorId = " + majorId +
            ", majorName = " + majorName +
            ", categoryId = " + categoryId +
        "}";
    }
}

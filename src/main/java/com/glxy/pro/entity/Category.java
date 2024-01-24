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
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 大类ID
     */
    private String categoryId;

    /**
     * 大类名称
     */
    private String categoryName;

    /**
     * 学生总数
     */
    private Integer stuNum;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getStuNum() {
        return stuNum;
    }

    public void setStuNum(Integer stuNum) {
        this.stuNum = stuNum;
    }

    @Override
    public String toString() {
        return "Category{" +
            "categoryId = " + categoryId +
            ", categoryName = " + categoryName +
            ", stuNum = " + stuNum +
        "}";
    }
}

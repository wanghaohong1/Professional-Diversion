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
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    private String stuId;

    /**
     * 学生姓名
     */
    private String stuName;

    /**
     * 性别:0——男，1——女
     */
    private Byte stuSex;

    /**
     * 年级
     */
    private Integer stuGrade;

    /**
     * 所属大类ID
     */
    private String categoryId;

    /**
     * 班级
     */
    private String stuClass;

    /**
     * 总绩点
     */
    private Object stuScore;

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public Byte getStuSex() {
        return stuSex;
    }

    public void setStuSex(Byte stuSex) {
        this.stuSex = stuSex;
    }

    public Integer getStuGrade() {
        return stuGrade;
    }

    public void setStuGrade(Integer stuGrade) {
        this.stuGrade = stuGrade;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getStuClass() {
        return stuClass;
    }

    public void setStuClass(String stuClass) {
        this.stuClass = stuClass;
    }

    public Object getStuScore() {
        return stuScore;
    }

    public void setStuScore(Object stuScore) {
        this.stuScore = stuScore;
    }

    @Override
    public String toString() {
        return "Student{" +
            "stuId = " + stuId +
            ", stuName = " + stuName +
            ", stuSex = " + stuSex +
            ", stuGrade = " + stuGrade +
            ", categoryId = " + categoryId +
            ", stuClass = " + stuClass +
            ", stuScore = " + stuScore +
        "}";
    }
}

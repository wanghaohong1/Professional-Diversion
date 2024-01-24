package com.glxy.pro.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@TableName("freshman_grades")
public class FreshmanGrades implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    private String stuId;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程学分
     */
    private Integer courseWeight;

    /**
     * 单科绩点
     */
    private Object coursePoint;

    /**
     * 单科成绩
     */
    private Object courseScore;

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseWeight() {
        return courseWeight;
    }

    public void setCourseWeight(Integer courseWeight) {
        this.courseWeight = courseWeight;
    }

    public Object getCoursePoint() {
        return coursePoint;
    }

    public void setCoursePoint(Object coursePoint) {
        this.coursePoint = coursePoint;
    }

    public Object getCourseScore() {
        return courseScore;
    }

    public void setCourseScore(Object courseScore) {
        this.courseScore = courseScore;
    }

    @Override
    public String toString() {
        return "FreshmanGrades{" +
            "stuId = " + stuId +
            ", courseName = " + courseName +
            ", courseWeight = " + courseWeight +
            ", coursePoint = " + coursePoint +
            ", courseScore = " + courseScore +
        "}";
    }
}

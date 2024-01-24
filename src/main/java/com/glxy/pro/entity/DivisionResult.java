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
@TableName("division_result")
public class DivisionResult implements Serializable {

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
     * 排名
     */
    private Integer ranking;

    /**
     * 高考折算成绩
     */
    private Object gaokaoFinalScore;

    /**
     * 大一折算成绩
     */
    private Object freshmanFinalScore;

    /**
     * 综合成绩
     */
    private Object finalScore;

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

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Object getGaokaoFinalScore() {
        return gaokaoFinalScore;
    }

    public void setGaokaoFinalScore(Object gaokaoFinalScore) {
        this.gaokaoFinalScore = gaokaoFinalScore;
    }

    public Object getFreshmanFinalScore() {
        return freshmanFinalScore;
    }

    public void setFreshmanFinalScore(Object freshmanFinalScore) {
        this.freshmanFinalScore = freshmanFinalScore;
    }

    public Object getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Object finalScore) {
        this.finalScore = finalScore;
    }

    @Override
    public String toString() {
        return "DivisionResult{" +
            "stuId = " + stuId +
            ", majorId = " + majorId +
            ", ranking = " + ranking +
            ", gaokaoFinalScore = " + gaokaoFinalScore +
            ", freshmanFinalScore = " + freshmanFinalScore +
            ", finalScore = " + finalScore +
        "}";
    }
}

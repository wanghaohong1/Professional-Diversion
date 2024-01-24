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
public class Gaokao implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学号
     */
    private String stuId;

    /**
     * 高考录取类型
     */
    private String stuFrom;

    /**
     * 文理分科:0——理，1——文
     */
    private Byte sciLib;

    /**
     * 分数线
     */
    private Object scoreLine;

    /**
     * 高考总分
     */
    private Object gkScore;

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuFrom() {
        return stuFrom;
    }

    public void setStuFrom(String stuFrom) {
        this.stuFrom = stuFrom;
    }

    public Byte getSciLib() {
        return sciLib;
    }

    public void setSciLib(Byte sciLib) {
        this.sciLib = sciLib;
    }

    public Object getScoreLine() {
        return scoreLine;
    }

    public void setScoreLine(Object scoreLine) {
        this.scoreLine = scoreLine;
    }

    public Object getGkScore() {
        return gkScore;
    }

    public void setGkScore(Object gkScore) {
        this.gkScore = gkScore;
    }

    @Override
    public String toString() {
        return "Gaokao{" +
            "stuId = " + stuId +
            ", stuFrom = " + stuFrom +
            ", sciLib = " + sciLib +
            ", scoreLine = " + scoreLine +
            ", gkScore = " + gkScore +
        "}";
    }
}

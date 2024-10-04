package com.glxy.pro.dto;

import lombok.Data;

/**
 * @author Alonha
 * @create 2023-11-28-16:00
 */
@Data
public class RankingDto {
    /**
     * 学号
     */
    private String stuId;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 年级
     */
    private String grade;

    /**
     * 所属大类名称
     */
    private String category;

    /**
     * 所属大类id
     */
    private String categoryId;

    /**
     * 排名
     */
    private Integer ranking;

    /**
     * 高考折算成绩
     */
    private Double gaokaoFinalScore;

    /**
     * 大一折算成绩
     */
    private Double freshmanFinalScore;

    /**
     * 综合成绩
     */
    private Double finalScore;

    /**
     * 科类
     */
    private String sciLib;

}

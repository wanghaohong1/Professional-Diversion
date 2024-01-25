package com.glxy.pro.DTO;

import lombok.Data;

import java.util.List;
@Data
public class ExportDto {
    /**
     * 学号
     */
    private String stuId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 班级
     */
    private String stuClass;

    /**
     * 所属大类
     */
    private String category;

    /**
     * 科类
     */
    private String sciLib;

    /**
     * 绩点
     */
    private String score;

    /**
     * 高考成绩
     */
    private String gkScore;

    /**
     * 高考录取批次线
     */
    private String scoreLine;

    /**
     * 排名
     */
    private String ranking;

    /**
     * 志愿列表
     */
    private List<VolunteerDTO> volunteerList;

    /**
     * 录取专业
     */
    private String admMajor;

}

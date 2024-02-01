package com.glxy.pro.service;

import com.glxy.pro.DTO.GradeManagePageDTO;
import com.glxy.pro.DTO.PageDTO;
import com.glxy.pro.DTO.RankingDTO;
import com.glxy.pro.query.StudentQuery;

/**
 * @author Alonha
 * @create 2024-01-31-0:43
 */
public interface IGradeService {

    PageDTO<GradeManagePageDTO> getGradeManagePage(StudentQuery query);

    /**
     * 获取排名页面数据
     * @param query 查询条件
     * @return 页面数据
     */
    PageDTO<RankingDTO> getRankingPage(StudentQuery query);

    /**
     * 计算综合成绩与排名
     *
     * @param gaokaoPer 高考成绩所占比例
     * @param categoryName 大类名称
     * @param sciLib 所属分科
     * @return 是否成功
     */
    void getFinalScoreAndRanking(Integer sciLib, double gaokaoPer, String categoryName);
}

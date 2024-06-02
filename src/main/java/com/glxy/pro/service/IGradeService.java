package com.glxy.pro.service;

import com.glxy.pro.dto.GradeManagePageDto;
import com.glxy.pro.dto.PageDto;
import com.glxy.pro.dto.RankingDto;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.bo.GaokaoBo;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.Gaokao;
import com.glxy.pro.query.StudentQuery;

import java.util.List;

/**
 * @author Alonha
 * @create 2024-01-31-0:43
 */
public interface IGradeService {

    PageDto<GradeManagePageDto> getGradeManagePage(StudentQuery query);

    /**
     * 获取排名页面数据
     * @param query 查询条件
     * @return 页面数据
     */
    PageDto<RankingDto> getRankingPage(StudentQuery query);

    ResultBody getFinalScoreAndRanking(Integer sciLib, double gaokaoPer, String categoryName);

    /**
     * 新增或删除高考成绩
     * @param gaokao 高考实体
     * @return 是否成功
     */
    boolean saveOrUpdateGaokao(Gaokao gaokao);

    /**
     * 增加或更新学生大一成绩
     * @param dto 学生成绩Dto
     * @return 是否成功
     */
    boolean saveOrUpdateFreshmanGrades(GradeManagePageDto dto);
}

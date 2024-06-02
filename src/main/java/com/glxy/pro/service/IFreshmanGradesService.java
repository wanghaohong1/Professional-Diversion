package com.glxy.pro.service;

import com.glxy.pro.dto.PageDto;
import com.glxy.pro.entity.FreshmanGrades;
import com.baomidou.mybatisplus.extension.service.IService;
import com.glxy.pro.query.FreshmanGradesQuery;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
public interface IFreshmanGradesService extends IService<FreshmanGrades> {

    List<FreshmanGrades> getFreshmanGradesById(String stuId);

    void removeFreshmanGradesByGrade(Integer currentGrade);

    boolean saveFreshmanGradesBatch(List<FreshmanGrades> importFreshmanGrades);

    void removeBatchByStuIds(List<String>ids);

    PageDto<FreshmanGrades> getFreshmanGradesByPagesAndConditions(FreshmanGradesQuery freshmanGradesQuery);

    /**
     * 修改学生大一单科成绩信息
     *
     * @param freshmanGrades 待修改的大一成绩信息（包含学号和课程名称）
     * @return 是否成功
     */
    boolean updateFreshmanGrades(FreshmanGrades freshmanGrades);
}

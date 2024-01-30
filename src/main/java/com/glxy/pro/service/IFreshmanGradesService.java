package com.glxy.pro.service;

import com.glxy.pro.entity.FreshmanGrades;
import com.baomidou.mybatisplus.extension.service.IService;

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
}

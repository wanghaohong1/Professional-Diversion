package com.glxy.pro.mapper;

import com.glxy.pro.entity.FreshmanGrades;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Mapper
public interface FreshmanGradesMapper extends BaseMapper<FreshmanGrades> {

    void removeFreshmanGradesByGrade(Integer grade);
}

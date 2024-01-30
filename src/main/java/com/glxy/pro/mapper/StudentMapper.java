package com.glxy.pro.mapper;

import com.glxy.pro.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Mapper
@Transactional
public interface StudentMapper extends BaseMapper<Student> {

    int removeStudentsByGrade(Integer grade);

    void updateScore(String stuId);

    int getStudentCountByCateId(String cateId);
}

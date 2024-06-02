package com.glxy.pro.service;

import com.glxy.pro.dto.PageDto;
import com.glxy.pro.bo.StudentBo;
import com.glxy.pro.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;
import com.glxy.pro.query.StudentQuery;
import org.apache.poi.ss.usermodel.BorderStyle;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
public interface IStudentService extends IService<Student> {

    void removeStudentsByGrade(Integer grade);

    void updateScore(String stuId);

    int getStudentCountByCateId(String cateId, Integer grade);

    PageDto<StudentBo> queryStudent(StudentQuery studentQuery);
}

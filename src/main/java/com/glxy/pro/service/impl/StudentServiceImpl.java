package com.glxy.pro.service.impl;

import com.glxy.pro.bo.StudentBo;
import com.glxy.pro.entity.Student;
import com.glxy.pro.mapper.StudentMapper;
import com.glxy.pro.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Override
    public Student getStudentById(String stuId) {
        return getById(stuId);
    }

}

package com.glxy.pro.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.glxy.pro.bo.StudentBo;
import com.glxy.pro.entity.Student;
import com.glxy.pro.mapper.StudentMapper;
import com.glxy.pro.mapper.UserMapper;
import com.glxy.pro.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public void removeStudentsByGrade(Integer grade) {
        studentMapper.removeStudentsByGrade(grade);
    }

    @Override
    public void updateScore(String stuId) {
        studentMapper.updateScore(stuId);
    }

    @Override
    public int getStudentCountByCateId(String cateId) {
        return studentMapper.getStudentCountByCateId(cateId);
    }

}

package com.glxy.pro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.glxy.pro.DTO.GradeManagePageDTO;
import com.glxy.pro.DTO.PageDTO;
import com.glxy.pro.DTO.RankingDTO;
import com.glxy.pro.bo.StudentBo;
import com.glxy.pro.entity.Student;
import com.glxy.pro.mapper.GradeMapper;
import com.glxy.pro.mapper.StudentMapper;
import com.glxy.pro.query.StudentQuery;
import com.glxy.pro.service.IAdmissionService;
import com.glxy.pro.service.ICategoryService;
import com.glxy.pro.service.IGradeService;
import com.glxy.pro.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alonha
 * @create 2024-01-31-0:43
 */
@Service
public class GradeServiceImpl implements IGradeService {

    @Autowired
    private GradeMapper gradeMapper;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IStudentService studentService;

    @Override
    public PageDTO<GradeManagePageDTO> getGradeManagePage(StudentQuery studentQuery) {
        PageDTO<GradeManagePageDTO> res = new PageDTO<>();
        int begin = (studentQuery.getPageNo() - 1) * studentQuery.getPageSize();
        List<GradeManagePageDTO> rankingList = gradeMapper.selectGradeManagePage(studentQuery, begin);
        Integer total = gradeMapper.selectGradeTotal(studentQuery);
        res.setList(rankingList);
        res.setPages((long) (total / studentQuery.getPageSize()) + 1);
        res.setTotal(Long.valueOf(total));
        return res;
    }

    @Override
    public PageDTO<RankingDTO> getRankingPage(StudentQuery studentQuery) {
        PageDTO<RankingDTO> res = new PageDTO<>();
        int begin = (studentQuery.getPageNo() - 1) * studentQuery.getPageSize();
        List<RankingDTO> rankingList = gradeMapper.selectRanking(studentQuery, begin);
        Integer total = gradeMapper.selectRankingTotal(studentQuery);
        res.setList(rankingList);
        res.setPages((long) (total / studentQuery.getPageSize()) + 1);
        res.setTotal(Long.valueOf(total));
        return res;
    }

    @Override
    public void getFinalScoreAndRanking(Integer sciLib, double gaokaoPer, String categoryName) {
        // 获取当前年份
        String currentGrade = String.valueOf(LocalDateTime.now().getYear() - 1);

        // 查当年该大类下的所有学生
        StudentQuery studentQuery = new StudentQuery();
        studentQuery.setGrade(currentGrade);
        studentQuery.setSciLib(sciLib);
        studentQuery.setCategoryId(categoryService.getCategoryByName(categoryName).getCategoryId());
        List<Student> students = studentService.queryStudent(studentQuery);
        // 获取学生id集合
        List<String> stuIds = students.stream()
                .map(Student::getStuId)
                .collect(Collectors.toList());
        // 删除所有学生的分流情况

    }
}

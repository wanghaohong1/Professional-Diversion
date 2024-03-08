package com.glxy.pro.service.impl;

import com.glxy.pro.DTO.GradeManagePageDTO;
import com.glxy.pro.DTO.PageDTO;
import com.glxy.pro.DTO.RankingDTO;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.bo.StudentBo;
import com.glxy.pro.common.CommonEnum;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.FreshmanGrades;
import com.glxy.pro.entity.Gaokao;
import com.glxy.pro.mapper.GradeMapper;
import com.glxy.pro.query.FreshmanGradesQuery;
import com.glxy.pro.query.StudentQuery;
import com.glxy.pro.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.extension.toolkit.Db.saveOrUpdate;
import static com.glxy.pro.constant.RedisConstants.GAOKAO_CACHE;

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
    @Autowired
    private IDivisionResultService divisionResultService;
    @Autowired
    private IGaokaoService gaokaoService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IFreshmanGradesService freshmanGradesService;


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
    public ResultBody getFinalScoreAndRanking(Integer sciLib, double gaokaoPer, String categoryName) {
        boolean flag = true;  // 用于表示是否有学生缺失高考数据或大一成绩

        // 获取当前年份
        String currentGrade = String.valueOf(LocalDateTime.now().getYear() - 1);
        List<DivisionResultBo> res = new ArrayList<>();

        // 查当年该大类下的所有学生
        StudentQuery studentQuery = new StudentQuery();
        studentQuery.setGrade(currentGrade);
        studentQuery.setSciLib(sciLib);
        studentQuery.setCategoryId(categoryService.getCategoryByName(categoryName).getCategoryId());
        List<StudentBo> studentBos = studentService.queryStudent(studentQuery).getList();
        // 获取学生id集合
        List<String> stuIds = studentBos.stream()
                .map(StudentBo::getStuId)
                .collect(Collectors.toList());
        // 删除所有学生的分流情况
        divisionResultService.removeBatchByIds(stuIds);
        for (StudentBo studentBo : studentBos) {
            // 创建分流结果对象并添加学号
            DivisionResultBo divisionResultBo = new DivisionResultBo();
            String stuId = studentBo.getStuId();
            divisionResultBo.setStuId(stuId);

            // 根据学号获取高考成绩和大一成绩
            if(studentBo.getScore() == null) {
                // 如果学生没有大一总绩点成绩，则跳过该学生
                flag = false;
                continue;
            }
            BigDecimal freshmanScore = BigDecimal.valueOf(studentBo.getScore());
            Gaokao gaokao = gaokaoService.getGaokaoById(stuId);
            if(gaokao == null || gaokao.getScoreLine() == null || gaokao.getGkScore() == null) {
                // 如果学生没有高考信息，或者没有高考成绩，或高考分数线，跳过该学生
                flag = false;
                continue;
            }
            BigDecimal gaokaoScore = BigDecimal.valueOf(gaokao.getGkScore());
            BigDecimal gaokaoScoreLine = BigDecimal.valueOf(gaokao.getScoreLine());

            // 计算并添加和高考折算成绩大一折算成绩
            BigDecimal gaokaoFinalScore = gaokaoScore.divide(gaokaoScoreLine, 12, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            BigDecimal freshmanFinalScore = freshmanScore.multiply(BigDecimal.valueOf(20)).setScale(12, RoundingMode.HALF_UP);
            divisionResultBo.setGaokaoFinalScore(gaokaoFinalScore.doubleValue());
            divisionResultBo.setFreshmanFinalScore(freshmanFinalScore.doubleValue());

            // 计算并添加综合成绩
            BigDecimal finalScore = gaokaoFinalScore.multiply(BigDecimal.valueOf(gaokaoPer))
                    .add(freshmanFinalScore.multiply(BigDecimal.valueOf(1 - gaokaoPer)))
                    .setScale(12, RoundingMode.HALF_UP);
            divisionResultBo.setFinalScore(finalScore.setScale(12, RoundingMode.HALF_UP).doubleValue());
            res.add(divisionResultBo);
        }
        // 对res列表里面的排名字段进行填充
        if(res.size() == 0) return ResultBody.success(CommonEnum.NO_INFO);
        fillRanking(res);
        ResultBody result = divisionResultService.saveDivisionResult(res);
        return flag && result.getCode() == 200 ? result : ResultBody.error("部分学生计算综合成绩失败，请检查是否存在数据缺失");
    }

    @Override
    public boolean saveOrUpdateGaokao(Gaokao gaokao) {
        // 删除缓存
        redisTemplate.delete(GAOKAO_CACHE + gaokao.getStuId());
        return saveOrUpdate(gaokao);
    }

    @Override
    public boolean saveOrUpdateFreshmanGrades(GradeManagePageDTO dto) {
        List<FreshmanGrades> freshmanGradesList = dto.getFreshmanGradesList();
        boolean freshmanGradesRes = false;
        for (FreshmanGrades freshmanGrades : freshmanGradesList) {
            // 根据学号和课程名称检索出大一成绩对象
            FreshmanGradesQuery query = new FreshmanGradesQuery();
            List<String> stuIds = new ArrayList<>();
            stuIds.add(freshmanGrades.getStuId());
            query.setCourseName(freshmanGrades.getCourseName());
            query.setStuIds(stuIds);
            if (freshmanGradesService.getFreshmanGradesByPagesAndConditions(query).getList().size() == 0) {
                freshmanGradesRes = freshmanGradesService.save(freshmanGrades);
            } else {
                String stuId = freshmanGrades.getStuId();
                String courseName = freshmanGrades.getCourseName();
                if (stuId != null || courseName != null) freshmanGradesRes = freshmanGradesService.updateFreshmanGrades(freshmanGrades);
            }
        }
        return freshmanGradesRes;
    }

    private void fillRanking(List<DivisionResultBo> resultList) {
        // 按照综合成绩进行降序排列
        resultList.sort(Comparator.comparing(DivisionResultBo::getFinalScore).reversed());
        // 分配排名
        int rank = 1;
        int sameScoreCount = 1;
        DivisionResultBo firstOne = resultList.get(0);
        firstOne.setRanking(rank);
        for (int i = 1; i < resultList.size(); i++) {
            DivisionResultBo currentResult = resultList.get(i);
            if (currentResult.getFinalScore() < resultList.get(i - 1).getFinalScore()) {
                rank += sameScoreCount;
                sameScoreCount = 1;
            } else {
                sameScoreCount++;
            }
            currentResult.setRanking(rank);
        }
    }
}

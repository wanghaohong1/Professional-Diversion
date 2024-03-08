package com.glxy.pro.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.glxy.pro.DTO.GradeListDTO;
import com.glxy.pro.DTO.GradeManagePageDTO;
import com.glxy.pro.DTO.PageDTO;
import com.glxy.pro.DTO.RankingDTO;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.bo.GaokaoBo;
import com.glxy.pro.common.CommonEnum;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.DivisionResult;
import com.glxy.pro.entity.FreshmanGrades;
import com.glxy.pro.entity.Gaokao;
import com.glxy.pro.query.FreshmanGradesQuery;
import com.glxy.pro.query.StudentQuery;
import com.glxy.pro.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.glxy.pro.common.CommonEnum.*;
import static com.glxy.pro.constant.CommonConstant.LOGIN_COOKIE;
import static com.glxy.pro.constant.RedisConstants.*;

@SaCheckLogin
@RestController
@Slf4j
@RequestMapping
public class GradeController {
    @Autowired
    private IGaokaoService gaokaoService;
    @Autowired
    private IFreshmanGradesService freshmanGradesService;
    @Autowired
    private IDivisionResultService divisionResultService;
    @Autowired
    private IGradeService gradeService;
    @Autowired
    private IDocumentService documentService;

    @Autowired
    private RedisTemplate redisTemplate;

    // ==================================== 学生端接口 ====================================

    @ApiOperation("获取学生成绩清单")
    @GetMapping("/student/grade/getGradeList")
    public ResultBody getGradeList(@CookieValue(value = LOGIN_COOKIE, required = false) String token) {
        // 用token获取学号
        if (token == null) return ResultBody.error(NEED_LOGIN);
        String stuId = redisTemplate.opsForValue().get(TOKEN_CACHE + token).toString();

        // 用学号获取成绩清单
        GradeListDTO gradeList = new GradeListDTO();
        // 先查缓存
        if (Boolean.TRUE.equals(redisTemplate.hasKey(GRADE_LIST_CACHE + stuId))) {
            // 缓存命中 直接返回
            gradeList = (GradeListDTO) redisTemplate.opsForValue().get(GRADE_LIST_CACHE + stuId);
            return ResultBody.success(gradeList);
        } else {
            // 缓存未命中 查数据库
            Gaokao gaokao = gaokaoService.getGaokaoById(stuId);
            List<FreshmanGrades> freshmanGradesList = freshmanGradesService.getFreshmanGradesById(stuId);
            DivisionResultBo divisionResult = divisionResultService.getDivisionResultById(stuId);
            // 结果装进DTO中
            gradeList.setFreshmanGradesList(freshmanGradesList);
            BeanUtils.copyProperties(divisionResult, gradeList);
            BeanUtils.copyProperties(gaokao, gradeList);
            if (BeanUtil.isNotEmpty(gradeList)) {
                // 数据库命中 构建缓存
                redisTemplate.opsForValue().set(GRADE_LIST_CACHE + stuId, gradeList);
                redisTemplate.expire(GRADE_LIST_CACHE + stuId, TWELVE_HOUR_TTL, TimeUnit.SECONDS);
                return ResultBody.success(gradeList);
            }else {
                // 数据库未命中 返回空
                return ResultBody.success(NO_INFO);
            }
        }
    }

    // ==================================== 管理员接口 ====================================
    @ApiOperation("获取成绩管理页面数据")
    @GetMapping("teacher/getGradeManagePages")
    public ResultBody getGradeManagePages(StudentQuery studentQuery) {
        PageDTO<GradeManagePageDTO> res = gradeService.getGradeManagePage(studentQuery);
        return res.getList().size() > 0 ? ResultBody.success(res) : ResultBody.success();
    }

    @ApiOperation("获取排名页面数据")
    @GetMapping("teacher/getRankingPages")
    public ResultBody getRankingPages(StudentQuery studentQuery) {
        PageDTO<RankingDTO> res = gradeService.getRankingPage(studentQuery);
        return res.getList().size() > 0 ? ResultBody.success(res) : ResultBody.success(CommonEnum.NO_INFO);
    }

    @ApiOperation("计算综合成绩")
    @GetMapping("teacher/getFinalScoreAndRanking")
    public ResultBody getFinalScoreAndRanking(@RequestParam("sciLib") Integer sciLib, @RequestParam("gaokaoPer") double gaokaoPer, @RequestParam("categoryName") String categoryName) {
        return gradeService.getFinalScoreAndRanking(sciLib, gaokaoPer, categoryName);
    }

    @ApiOperation("新增或修改成绩")
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("teacher/saveOrUpdateGrades")
    public ResultBody saveOrUpdateGrades(@RequestBody GradeManagePageDTO dto) {
        Gaokao gaokao = new Gaokao();
        BeanUtils.copyProperties(dto, gaokao);
        boolean gaokaoRes = gaokaoService.saveOrUpdate(gaokao);
        if (!gaokaoRes) return ResultBody.error("高考成绩操作失败");
        if (dto.getFreshmanGradesList() != null && dto.getFreshmanGradesList().size() > 0) {
            boolean freshmanGradeRes = gradeService.saveOrUpdateFreshmanGrades(dto);
            if (!freshmanGradeRes) return ResultBody.error("课程成绩操作失败");
            documentService.updateScore(dto.getStuId());
        }
        return ResultBody.success();

    }

    @ApiOperation("根据学号查询高考成绩")
    @GetMapping("teacher/gaokao/getById")
    public ResultBody getGaokaoById(@RequestParam("stuId") String stuId) {
        Gaokao byId = gaokaoService.getGaokaoById(stuId);
        return byId == null ? ResultBody.error(CommonEnum.DATA_NOT_EXIST) : ResultBody.success(byId);
    }

    @ApiOperation("根据学号查询大一成绩")
    @GetMapping("teacher/freshmanGrades/getById")
    public ResultBody getFreshmanGradesById(@RequestParam("stuId") String stuId) {
        if(stuId == null) return ResultBody.error(CommonEnum.PARAM_REQUIRE);
        List<FreshmanGrades> list = freshmanGradesService.getFreshmanGradesById(stuId);
        return list == null ||list.size() == 0 ? ResultBody.error(DATA_NOT_EXIST) : ResultBody.success(list);
    }

    @ApiOperation("增加学生高考信息")
    @PostMapping("teacher/gaokao/save")
    public ResultBody saveGaokao(@RequestBody Gaokao gaokao) {
        boolean result = gaokaoService.save(gaokao);
        return result ? ResultBody.success() : ResultBody.error(CommonEnum.DATA_EXIST);
    }

    @ApiOperation("增加学生大一成绩")
    @PostMapping("teacher/freshmanGrades/save")
    public ResultBody saveFreshmanGrades(@RequestBody FreshmanGrades freshmanGrades) {
        boolean res = freshmanGradesService.save(freshmanGrades);
        return res ? ResultBody.success() : ResultBody.error("修改数据失败");
    }

    @ApiOperation("修改学生高考信息")
    @PutMapping("teacher/gaokao/update")
    public ResultBody updateGaokao(@RequestBody Gaokao gaokao) {
        boolean res = gaokaoService.updateGaokao(gaokao);
        return res ? ResultBody.success() : ResultBody.error("修改数据失败");
    }

    @ApiOperation("修改学生大一单科成绩信息")
    @PutMapping("teacher/freshmanGrades/update")
    public ResultBody updateFreshmanGrades(@RequestBody FreshmanGrades freshmanGrades) {
        String stuId = freshmanGrades.getStuId();
        String courseName = freshmanGrades.getCourseName();
        if (stuId == null || courseName == null) return ResultBody.error(PARAM_REQUIRE);
        boolean res = freshmanGradesService.updateFreshmanGrades(freshmanGrades);
        return res ? ResultBody.success() : ResultBody.error("修改数据失败");
    }

    @ApiOperation("删除全部学生高考成绩")
    @DeleteMapping("teacher/gaokao/removeAll")
    public ResultBody removeAllGaokao() {
        boolean res = gaokaoService.remove(new QueryWrapper<>());
        return res ? ResultBody.success() : ResultBody.error(CommonEnum.DATA_NOT_EXIST);
    }

    @ApiOperation("删除全部学生大一成绩")
    @DeleteMapping("teacher/freshmanGrades/removeAll")
    public ResultBody removeAllFreshmanGrades() {
        boolean res = freshmanGradesService.remove(new QueryWrapper<>());
        return res ? ResultBody.success() : ResultBody.error(CommonEnum.DATA_NOT_EXIST);
    }

}
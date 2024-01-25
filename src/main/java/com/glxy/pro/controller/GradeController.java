package com.glxy.pro.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.glxy.pro.DTO.GradeListDTO;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.DivisionResult;
import com.glxy.pro.entity.FreshmanGrades;
import com.glxy.pro.entity.Gaokao;
import com.glxy.pro.service.IDivisionResultService;
import com.glxy.pro.service.IFreshmanGradesService;
import com.glxy.pro.service.IGaokaoService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.glxy.pro.common.CommonEnum.NEED_LOGIN;
import static com.glxy.pro.constant.RedisConstants.TOKEN_CACHE;

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
    private StringRedisTemplate redisTemplate;

    /**
     * 获取学生成绩清单
     *
     * @return 成绩清单
     */
    @ApiOperation("获取学生成绩清单")
    @GetMapping("/student/grade/getGradeList")
    public ResultBody getGradeList(@CookieValue(value = "satoken", required = false) String token) {
        // 用token获取学号
        if (token == null) return ResultBody.error(NEED_LOGIN);
        String stuId = redisTemplate.opsForValue().get(TOKEN_CACHE + token);
        // 获取成绩清单
        GradeListDTO gradeList = new GradeListDTO();
        Gaokao gaokao = gaokaoService.getGaokaoById(stuId);
        List<FreshmanGrades> freshmanGradesList = freshmanGradesService.getFreshmanGradesById(stuId);
        DivisionResultBo divisionResult = divisionResultService.getDivisionResultById(stuId);
        // 装进DTO中
        gradeList.setFreshmanGradesList(freshmanGradesList);
        BeanUtils.copyProperties(divisionResult, gradeList);
        BeanUtils.copyProperties(gaokao, gradeList);
        return ResultBody.success(gradeList);
    }
}
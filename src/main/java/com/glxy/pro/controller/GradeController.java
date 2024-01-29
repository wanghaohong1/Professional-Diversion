package com.glxy.pro.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.bean.BeanUtil;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.glxy.pro.common.CommonEnum.NEED_LOGIN;
import static com.glxy.pro.common.CommonEnum.NO_INFO;
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
    private RedisTemplate redisTemplate;

    /**
     * 获取学生成绩清单
     *
     * @return 成绩清单
     */
    @ApiOperation("获取学生成绩清单")
    @GetMapping("/student/grade/getGradeList")
    public ResultBody getGradeList(@CookieValue(value = LOGIN_COOKIE, required = false) String token) {
        // 用token获取学号
        if (token == null) return ResultBody.error(NEED_LOGIN);
        String stuId = String.valueOf(redisTemplate.opsForValue().get(TOKEN_CACHE + token));
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
}
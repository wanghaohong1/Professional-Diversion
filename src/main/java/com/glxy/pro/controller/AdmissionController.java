package com.glxy.pro.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.glxy.pro.bo.AdmissionBo;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.DivisionResult;
import com.glxy.pro.service.IAdmissionService;
import com.glxy.pro.service.IDivisionResultService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

import static com.glxy.pro.common.CommonEnum.NEED_LOGIN;
import static com.glxy.pro.common.CommonEnum.NO_INFO;
import static com.glxy.pro.constant.CommonConstant.LOGIN_COOKIE;
import static com.glxy.pro.constant.RedisConstants.TOKEN_CACHE;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Slf4j
@SaCheckLogin
@RestController
@RequestMapping
public class AdmissionController {
    @Autowired
    private IAdmissionService admissionService;
    @Autowired
    private IDivisionResultService divisionResultService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("查看往年专业录取情况")
    @GetMapping("/student/admission/getAdmissionByYear")
    public ResultBody getAdmissionByYear(@RequestParam("admYear") Integer admYear, @RequestParam("categoryName") String categoryName) {
        List<AdmissionBo> res = admissionService.getAdmissionByYear(categoryName, admYear);
        return res == null ? ResultBody.success(NO_INFO) : ResultBody.success(res);
    }

    @ApiOperation("学生查看录取结果")
    @GetMapping("/student/admission/getDivisionResult")
    public ResultBody getDivisionResult(@CookieValue(value = LOGIN_COOKIE, required = false) String token) {
        // 用token获取学号
        if (token == null) return ResultBody.error(NEED_LOGIN);
        String stuId = String.valueOf(redisTemplate.opsForValue().get(TOKEN_CACHE + token));
        DivisionResultBo res = divisionResultService.getDivisionResultById(stuId);
        return res == null ? ResultBody.success(NO_INFO) : ResultBody.success(res);
    }
}

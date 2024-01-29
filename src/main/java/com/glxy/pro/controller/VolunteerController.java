package com.glxy.pro.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.glxy.pro.bo.AdmissionBo;
import com.glxy.pro.bo.VolunteerBo;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.Admission;
import com.glxy.pro.entity.Major;
import com.glxy.pro.entity.Volunteer;
import com.glxy.pro.service.IAdmissionService;
import com.glxy.pro.service.ICategoryService;
import com.glxy.pro.service.IMajorService;
import com.glxy.pro.service.IVolunteerService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.glxy.pro.common.CommonEnum.*;
import static com.glxy.pro.constant.CommonConstant.LOGIN_COOKIE;
import static com.glxy.pro.constant.RedisConstants.TOKEN_CACHE;
import static com.glxy.pro.constant.RedisConstants.VOLUNTEER_CACHE;

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
public class VolunteerController {
    @Autowired
    private IVolunteerService volunteerService;
    @Autowired
    private IAdmissionService admissionService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation("学号查询志愿信息")
    @GetMapping("/student/volunteer/getById")
    public ResultBody getVolunteerById(@CookieValue(value = LOGIN_COOKIE, required = false) String token) {
        if (token == null) return ResultBody.error(NEED_LOGIN);
        String stuId = redisTemplate.opsForValue().get(TOKEN_CACHE + token);
        List<VolunteerBo> ById = volunteerService.getVolunteerById(stuId);
        if (ById == null) {
            return ResultBody.success(DATA_NOT_EXIST);
        } else {
            return ResultBody.success(ById);
        }
    }

    @ApiOperation("学生重置自己的志愿")
    @DeleteMapping("/student/volunteer/reset")
    public ResultBody resetVolunteer(@CookieValue(value = LOGIN_COOKIE, required = false) String token) {
        if (token == null) return ResultBody.error(NEED_LOGIN);
        String stuId = redisTemplate.opsForValue().get(TOKEN_CACHE + token);

        return volunteerService.resetVolunteers(stuId) ? ResultBody.success() : ResultBody.error(DATA_NOT_EXIST);
    }

    @ApiOperation("添加或修改志愿信息")
    @PostMapping("/student/volunteer/saveOrUpdate")
    public ResultBody saveOrUpdateVolunteer(@CookieValue(value = LOGIN_COOKIE, required = false) String token,
                                            @RequestBody List<Volunteer> volunteerList) {
        if (token == null) return ResultBody.error(NEED_LOGIN);
        String stuId = redisTemplate.opsForValue().get(TOKEN_CACHE + token);
        // 删除缓存
        redisTemplate.delete(VOLUNTEER_CACHE + stuId);
        // 确认是更新还是新增
        boolean save = volunteerService.list(new LambdaQueryWrapper<Volunteer>().eq(Volunteer::getStuId, stuId)).isEmpty();
        // 循环添加或修改
        boolean isSucc = true;
        for (Volunteer volunteer : volunteerList) {
            if (save) {
                // 添加
                volunteer.setStuId(stuId);
                isSucc = volunteerService.save(volunteer);
            } else {
                // 修改
                LambdaQueryWrapper<Volunteer> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Volunteer::getStuId, stuId)
                        .eq(Volunteer::getMajorId, volunteer.getMajorId());
                isSucc = volunteerService.update(volunteer, wrapper);
            }
        }
        if (isSucc) {
            if (save) return ResultBody.success("志愿添加成功");
            else return ResultBody.success("志愿修改成功");
        } else {
            if (save) return ResultBody.error("志愿添加失败");
            else return ResultBody.error("志愿修改失败");
        }
    }

    @ApiOperation("查看今年专业招生计划")
    @GetMapping("/student/volunteer/getCurrentAdmission")
    public ResultBody getCurrentAdmission(@RequestParam("categoryName") String categoryName) {
        List<AdmissionBo> res = admissionService.getAdmissionByYear(categoryName, LocalDate.now().getYear());
        return res.isEmpty() ? ResultBody.success(NO_INFO) : ResultBody.success(res);
    }
}

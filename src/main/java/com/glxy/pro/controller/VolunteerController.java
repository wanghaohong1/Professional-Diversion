package com.glxy.pro.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.glxy.pro.bo.AdmissionBo;
import com.glxy.pro.bo.StudentBo;
import com.glxy.pro.bo.VolunteerBo;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.Admission;
import com.glxy.pro.entity.Major;
import com.glxy.pro.entity.Student;
import com.glxy.pro.entity.Volunteer;
import com.glxy.pro.query.StudentQuery;
import com.glxy.pro.query.VolunteerQuery;
import com.glxy.pro.service.*;
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
import java.util.Set;
import java.util.stream.Collectors;

import static com.glxy.pro.common.CommonEnum.*;
import static com.glxy.pro.constant.CommonConstant.LOGIN_COOKIE;
import static com.glxy.pro.constant.RedisConstants.*;

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
    private IStudentService studentService;

    @Autowired
    private StringRedisTemplate redisTemplate;


    // ==================================== 管理员接口 ====================================

    @ApiOperation("获取志愿填报通道的开启时间")
    @GetMapping("/teacher/volunteer/getSysStart")
    public ResultBody getSysStart() {
        String start = redisTemplate.opsForValue().get(VOLUNTEER_START_TIME);
        return start != null ? ResultBody.success(start) : ResultBody.success(NO_INFO);
    }

    @ApiOperation("获取志愿填报通道的结束时间")
    @GetMapping("/teacher/volunteer/getSysEnd")
    public ResultBody getSysEnd() {
        String end = redisTemplate.opsForValue().get(VOLUNTEER_END_TIME);
        return end != null ? ResultBody.success(end) : ResultBody.success(NO_INFO);
    }

    @ApiOperation("设置志愿填报通道的开启和结束时间")
    @GetMapping("/teacher/volunteer/setSysStartAndEnd")
    public ResultBody setSysStartAndEnd(@RequestParam("start") String start, @RequestParam("end") String end) {
        redisTemplate.opsForValue().set(VOLUNTEER_START_TIME, start);
        redisTemplate.opsForValue().set(VOLUNTEER_END_TIME, end);
        String startInRedis = redisTemplate.opsForValue().get(VOLUNTEER_START_TIME);
        String endInRedis = redisTemplate.opsForValue().get(VOLUNTEER_END_TIME);
        return start.equals(startInRedis) && end.equals(endInRedis) ? ResultBody.success("设置成功") : ResultBody.error("设置失败，请重试");
    }

    @ApiOperation("根据大类ID获取未填报学生")
    @GetMapping("/teacher/volunteer/getUnFillByCateId")
    public ResultBody getUnFillByCateId(@RequestParam("cateId") String cateId, String stuName) {
        // 添加年级限制
        List<StudentBo> unFillStudents = volunteerService.getUnFillStuByCateId(cateId, stuName, LocalDate.now().getYear()-1);
        return ResultBody.success(unFillStudents);
    }

    @ApiOperation("根据大类ID获取志愿填报率")
    @GetMapping("/teacher/volunteer/getFilledRateByCateId")
    public ResultBody getFilledRateByCateId(@RequestParam("cateId") String cateId) {
        // 获取大类下已填报的学生数量
        int filled = volunteerService.getFillCountByCateId(cateId);
        // 获取大类下所有学生数量
        int total = studentService.getStudentCountByCateId(cateId, LocalDate.now().getYear() - 1);
        // 计算填报率
        double filledRate = (double) filled / total;
        // 将填报率转换为百分比字符串，保留两位小数
        String filledRatePercentage = String.format("%.2f", filledRate * 100);
        return ResultBody.success(filledRatePercentage);
    }

    @ApiOperation("根据年份和大类获取招生计划")
    @GetMapping("/teacher/volunteer/getAdmByCateName")
    public ResultBody getAdmByCateName(@RequestParam("admYear") Integer admYear, @RequestParam("categoryName") String categoryName) {
        List<AdmissionBo> res = admissionService.getAdmissionByYearAndCate(categoryName, admYear);
        return res.isEmpty() ? ResultBody.success(NO_INFO) : ResultBody.success(res);
    }

    @ApiOperation("多条件分页查询志愿信息")
    @GetMapping("/teacher/volunteer/getVolunteerPage")
    public ResultBody getByPageAndCondition(StudentQuery query) {
        return ResultBody.success(volunteerService.getVolunteerByPagesAndConditions(query));
    }

    @ApiOperation("新增或修改学生志愿")
    @PostMapping("/teacher/volunteer/saveOrUpdate")
    public ResultBody saveOrUpdateVolunteer(@RequestBody List<Volunteer> volunteerList) {
        String stuId = volunteerList.get(0).getStuId();
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

    @ApiOperation("删除某学生的某个志愿")
    @DeleteMapping("/teacher/volunteer/removeOneVolunteerById")
    public ResultBody removeOneVolunteerById(@RequestParam("stuId") String stuId, @RequestParam("majorId") String majorId) {
        if (stuId == null) return ResultBody.error(PARAM_REQUIRE);
        // 删除缓存
        redisTemplate.delete(VOLUNTEER_CACHE + stuId);
        // 删数据库
        boolean res = volunteerService.lambdaUpdate()
                .eq(Volunteer::getStuId, stuId)
                .eq(Volunteer::getMajorId, majorId)
                .remove();
        return res ? ResultBody.success() : ResultBody.error(DATA_NOT_EXIST);
    }

    @ApiOperation("根据学号删除学生志愿")
    @DeleteMapping("/teacher/volunteer/removeById")
    public ResultBody removeVolunteerById(@RequestParam("stuId") String stuId) {
        if (stuId == null) return ResultBody.error(PARAM_REQUIRE);
        // 删除缓存
        redisTemplate.delete(VOLUNTEER_CACHE + stuId);
        // 删数据库
        boolean res = volunteerService.lambdaUpdate().eq(Volunteer::getStuId, stuId).remove();
        return res ? ResultBody.success() : ResultBody.error(DATA_NOT_EXIST);
    }

    @ApiOperation("批量删除学生志愿")
    @DeleteMapping("/teacher/volunteer/removeByIds")
    public ResultBody removeByIds(@RequestParam List<String> stuIds) {
        if (stuIds.size() == 0) return ResultBody.error(PARAM_REQUIRE);
        // 删除缓存
        List<String> redisKeys = new ArrayList<>();
        for (String stuId : stuIds) {
            redisKeys.add(VOLUNTEER_CACHE + stuId);
        }
        redisTemplate.delete(redisKeys);
        // 删数据库
        volunteerService.lambdaUpdate().in(Volunteer::getStuId, stuIds).remove();
        return ResultBody.success();
    }

    @ApiOperation("查询单个学生的志愿")
    @GetMapping("/teacher/volunteer/getVolunteerById")
    public ResultBody getVolunteerByStuId(@RequestParam("stuId") String stuId) {
        if (stuId == null) return ResultBody.error(PARAM_REQUIRE);
        List<VolunteerBo> res = volunteerService.getVolunteerById(stuId);
        return res.isEmpty() ? ResultBody.success(NO_INFO) : ResultBody.success(res);
    }

    // ==================================== 学生接口 ====================================

    @ApiOperation("学号查询志愿信息")
    @GetMapping("/student/volunteer/getById")
    public ResultBody getVolunteerById(@CookieValue(value = LOGIN_COOKIE, required = false) String token) {
        if (token == null) return ResultBody.error(NEED_LOGIN);
        String stuId = redisTemplate.opsForValue().get(TOKEN_CACHE + token);
        List<VolunteerBo> ById = volunteerService.getVolunteerById(stuId);
        return ResultBody.success(ById);
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
        List<AdmissionBo> res = admissionService.getAdmissionByYearAndCate(categoryName, LocalDate.now().getYear());
        return res.isEmpty() ? ResultBody.success(NO_INFO) : ResultBody.success(res);
    }
}

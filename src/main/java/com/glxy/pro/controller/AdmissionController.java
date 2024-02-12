package com.glxy.pro.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.glxy.pro.DTO.PageDTO;
import com.glxy.pro.bo.AdmissionBo;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.common.CommonEnum;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.constant.RedisConstants;
import com.glxy.pro.entity.Admission;
import com.glxy.pro.entity.Category;
import com.glxy.pro.entity.DivisionResult;
import com.glxy.pro.entity.Major;
import com.glxy.pro.query.AdmissionQuery;
import com.glxy.pro.query.DivisionResultQuery;
import com.glxy.pro.service.IAdmissionService;
import com.glxy.pro.service.ICategoryService;
import com.glxy.pro.service.IDivisionResultService;
import com.glxy.pro.service.IMajorService;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private ICategoryService categoryService;

    @Autowired
    private IMajorService majorService;

    @Autowired
    private RedisTemplate redisTemplate;

    // ==================================== 学生端接口 ====================================

    @ApiOperation("查看往年专业录取情况")
    @GetMapping("/student/admission/getAdmissionByYear")
    public ResultBody getAdmissionByYear(@RequestParam("admYear") Integer admYear, @RequestParam("categoryName") String categoryName) {
        List<AdmissionBo> res = admissionService.getAdmissionByYearAndCate(categoryName, admYear);
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

    @ApiOperation("根据大类ID获取大类信息")
    @GetMapping("/student/admission/getCategory")
    public ResultBody getCategory(@RequestParam("categoryId") String categoryId) {
        Category category = categoryService.getOne(new QueryWrapper<Category>().eq("category_id", categoryId));
        return (category == null ? ResultBody.error(CommonEnum.NO_INFO) : ResultBody.success(category));
    }

    // ==================================== 管理员接口 ====================================
    @ApiOperation("管理员设置本年度专业招生计划")
    @PostMapping("/teacher/admission/setEnrollmentPlan")
    public ResultBody setEnrollmentPlan(@RequestBody AdmissionBo admissionBo) {
        // 返回值
        CommonEnum result = admissionService.setEnrollmentPlan(admissionBo);
        // 判断是否生成或者更改本年度专业招生计划成功
        return result == CommonEnum.SUCCESS ? ResultBody.success() : ResultBody.error(result);
    }

    @ApiOperation("管理员对每年分流情况分页查询")
    @GetMapping("/teacher/admission/admissionPage")
    public ResultBody queryAdmission(AdmissionQuery query) {
        PageDTO<AdmissionBo> pageDTO = admissionService.queryAdmissionPage(query);
        return pageDTO == null ? ResultBody.success(NO_INFO) : ResultBody.success(pageDTO);
    }

    @ApiOperation("根据年份和某大类下的专业的ID集合获取该大类的招生计划集合")
    @GetMapping("/teacher/admission/getAdmissionByYearAndIds")
    public List<AdmissionBo> getAdmissionByYearAndIds(@RequestParam("majorIds") List<String> majorIds, @RequestParam("year") int year) {
        List<Admission> admissionList = admissionService.lambdaQuery()
                .in(Admission::getMajorId, majorIds)
                .eq(Admission::getAdmYear, year)
                .list();
        List<AdmissionBo> admissionBoList = new ArrayList<>();
        for (Admission admission : admissionList) {
            AdmissionBo admissionBo = new AdmissionBo();
            BeanUtils.copyProperties(admission, admissionBo);
            admissionBoList.add(admissionBo);
        }
        return admissionBoList;
    }

    @ApiOperation("管理员删除招生计划")
    @DeleteMapping("/teacher/admission/deleteAdmission")
    public ResultBody deleteAdmission(@RequestParam("majorId") String majorId, @RequestParam("admYear") Integer admYear) {
        if (admissionService.remove(new QueryWrapper<Admission>()
                .eq("major_id", majorId)
                .eq("adm_year", admYear))) {
            return ResultBody.success(CommonEnum.SUCCESS);
        } else {
            return ResultBody.error("删除失败");
        }
    }

    @ApiOperation("批量删除招生计划")
    @DeleteMapping("/teacher/admission/deleteAdmissionBatch")
    public ResultBody deleteAdmissionBatch(@RequestParam("majorIds") List<String> majorIds) {
        if (admissionService.remove(new QueryWrapper<Admission>()
                .in("major_id", majorIds)
                .eq("adm_year", Calendar.getInstance().get(Calendar.YEAR)))){
            return ResultBody.success(CommonEnum.SUCCESS);
        } else {
            return ResultBody.success("请先选择招生计划");
        }
    }

    @ApiOperation("根据大类Id获取该大类下的招生计划")
    @GetMapping("/teacher/admission/getAdmissionByCategoryId")
    public ResultBody getAdmissionByCategoryId(@RequestParam("categoryId")String categoryId) {
        return ResultBody.success(admissionService.getAdmissionByCategoryId(categoryId, LocalDateTime.now().getYear()));
    }

    @ApiOperation("获取所有的大类信息")
    @GetMapping("/teacher/admission/getCategoryList")
    public ResultBody getCategoryList() {
        List<Category> list = categoryService.list();
        return ResultBody.success(list);
    }

//    @ApiOperation("根据大类名称获取大类ID")
//    @GetMapping("/getCategoryId")
//    public ResultBody getCategoryId(@RequestParam("categoryName") String categoryName) {
//        List<Category> categoryList = categoryService.lambdaQuery()
//                .eq(Category::getCategoryName, categoryName)
//                .list();
//        return (categoryList.size() == 0 ? ResultBody.error(CommonEnum.NO_INFO) : ResultBody.success(categoryList.get(0).getCategoryId()));
//    }

    @ApiOperation("管理员新增或修改大类")
    @GetMapping("/teacher/admission/addCategory")
    public ResultBody saveOrUpdateCategory(@RequestParam("categoryName") String categoryName, @RequestParam("stuNum") Integer stuNum) {
        //判断是否生成成功
        if (categoryService.saveOrUpdateCategory(categoryName, stuNum)) {
            return ResultBody.success(CommonEnum.SUCCESS);
        } else {
            return ResultBody.error(CommonEnum.SERVER_ERROR);
        }
    }

    @ApiOperation("删除大类")
    @DeleteMapping("/teacher/admission/deleteCategory")
    public ResultBody deleteCategory(@RequestParam("categoryId") String categoryId) {
        if (categoryService.removeById(categoryId)) {
            return ResultBody.success(CommonEnum.SUCCESS);
        } else {
            return ResultBody.error("大类删除失败");
        }
    }

    @ApiOperation("获取所有大类信息")
    @GetMapping("/teacher/admission/getAllCategory")
    public ResultBody getAllCategory() {
        List<Category> categoryList = categoryService.lambdaQuery().list();
        if(categoryList.size() < 1){
            return ResultBody.success(CommonEnum.NO_INFO);
        }else {
            return ResultBody.success(categoryList);
        }
    }

    @ApiOperation("管理员对学生录取结果分页查询")
    @GetMapping("/teacher/admission/DivisionResultPage")
    public ResultBody queryAdmission(DivisionResultQuery query) {
        PageDTO<DivisionResultBo> pageDTO = divisionResultService.queryDivisionResultPage(query);
        return pageDTO == null ? ResultBody.success(NO_INFO) : ResultBody.success(pageDTO);
    }

    @ApiOperation("修改学生排名")
    @PutMapping("/teacher/admission/updateRanking")
    public ResultBody updateRanking(@RequestBody DivisionResultBo divisionResultBo){
        DivisionResult divisionResult = new DivisionResult();
        BeanUtils.copyProperties(divisionResultBo, divisionResult);
        boolean res = divisionResultService.updateById(divisionResult);
        return res ? ResultBody.success() : ResultBody.error("排名修改失败");
    }

    @ApiOperation("根据专业ID获取专业信息")
    @GetMapping("/teacher/admission/getMajor")
    public ResultBody getMajor(@RequestParam("majorId")String majorId) {
        Major major = majorService.getOne(new QueryWrapper<Major>().eq("major_id", majorId));
        return (major == null ? ResultBody.error(CommonEnum.NO_INFO) : ResultBody.success(major));
    }

    @ApiOperation("根据大类ID获取专业信息列表")
    @GetMapping("/teacher/admission/getMajorByCategoryId")
    public ResultBody getMajorByCategoryId(@RequestParam("categoryId")String categoryId) {
        List<Major> majors = majorService.lambdaQuery()
                .eq(Major::getCategoryId, categoryId).list();
        return (ResultBody.success(majors));
    }

    @ApiOperation("管理员新增专业")
    @GetMapping("/teacher/admission/addMajor")
    public ResultBody saveOrUpdateMajor(@RequestParam("majorName") String majorName, @RequestParam("categoryId") String categoryId) {
        List<Major> majors = majorService.lambdaQuery()
                .eq(Major::getMajorName, majorName)
                .list();
        if(majors.size() != 0) {
            return ResultBody.error("该专业已经存在，请勿重复添加");
        }
        Major major = new Major();
        major.setMajorId(UUID.randomUUID().toString());
        major.setMajorName(majorName);
        major.setCategoryId(categoryId);

        if(majorService.saveOrUpdate(major)) {
            return ResultBody.success(CommonEnum.SUCCESS);
        }else {
            return ResultBody.error(CommonEnum.SERVER_ERROR);
        }
    }

    @ApiOperation("根据大类ID获取对应所有专业ID集合")
    @GetMapping("/teacher/admission/getMajorIdsByCategoryId")
    public ResultBody getMajorIdsByCategoryId(@RequestParam("categoryId") String categoryId){
        List<Major> majorList = majorService.lambdaQuery()
                .eq(Major::getCategoryId, categoryId)
                .list();
        return ResultBody.success(majorList.stream()
                .map(Major::getMajorId)
                .collect(Collectors.toList()));
    }

    @ApiOperation("管理员修改专业")
    @PutMapping("/teacher/admission/updateMajor")
    public ResultBody updateMajor(@RequestBody Major major) {
        if(majorService.lambdaUpdate()
                .eq(Major::getMajorId, major.getMajorId())
                .set(Major::getMajorName, major.getMajorName())
                .update()){
            return ResultBody.success(CommonEnum.SUCCESS);
        }else {
            return ResultBody.error(CommonEnum.SERVER_ERROR);
        }
    }

    @ApiOperation("管理员删除专业")
    @DeleteMapping("/teacher/admission/deleteMajor")
    public ResultBody deleteMajor(@RequestParam("majorId")String majorId) {
        if(majorService.removeById(majorId)) {
            // 把对应的招生计划一起删了
            admissionService.remove(new QueryWrapper<Admission>()
                    .eq("major_id", majorId));
            return ResultBody.success();
        }else {
            return ResultBody.error("专业删除失败");
        }
    }

    @ApiOperation("获取所有的专业")
    @GetMapping("/teacher/admission/getAllMajor")
    public ResultBody getAllMajor() {
        List<Major> list = majorService.list();
        return ResultBody.success(list);
    }

    @ApiOperation("自动完成专业录取")
    @GetMapping("/teacher/admission/autoAdmission")
    public ResultBody autoAdmission() {
        return admissionService.autoAdmission(Calendar.getInstance().get(Calendar.YEAR));
    }

    @ApiOperation("自动完成补录")
    @GetMapping("/teacher/admission/blue")
    public ResultBody supplementation(HttpServletRequest request, HttpServletResponse response) {
        return admissionService.supplementation(Calendar.getInstance().get(Calendar.YEAR));
    }

    @ApiOperation("完成导入")
    @PostMapping("/teacher/admission/finishImport")
    public ResultBody finishImport() {
        redisTemplate.opsForValue().set(RedisConstants.IS_IMPORT, 1);
        return ResultBody.success();
    }

    @ApiOperation("完成专业设置")
    @PostMapping("/teacher/admission/settingAdmPlan")
    public ResultBody settingAdmPlan() {
        redisTemplate.opsForValue().set(RedisConstants.IS_PLAN_SETTING, 1);
        return ResultBody.success();
    }

    @ApiOperation("完成志愿填报时间段设置")
    @PostMapping("/teacher/admission/settingVolunteerTime")
    public ResultBody settingVolunteerTime() {
        redisTemplate.opsForValue().set(RedisConstants.IS_VOLUNTEER_TIME_SETTING, 1);
        return ResultBody.success();
    }

    @ApiOperation("执行录取")
    @PostMapping("/teacher/admission/execAdmission")
    public ResultBody execAdmission() {
        redisTemplate.opsForValue().set(RedisConstants.IS_EXEC_ADMISSION, 1);
        return ResultBody.success();
    }

    @ApiOperation("发布录取")
    @PostMapping("/teacher/admission/publishAdmission")
    public ResultBody publishAdmission() {
        redisTemplate.opsForValue().set(RedisConstants.IS_PUBLISH_ADMISSION, 1);
        return ResultBody.success();
    }

    @ApiOperation("重置录取状态")
    @PostMapping("/teacher/admission/resetAdmState")
    public ResultBody resetAdmState() {
        redisTemplate.delete(RedisConstants.IS_EXEC_ADMISSION);
        redisTemplate.delete(RedisConstants.IS_PUBLISH_ADMISSION);
        redisTemplate.delete(RedisConstants.IS_PLAN_SETTING);
        redisTemplate.delete(RedisConstants.IS_VOLUNTEER_TIME_SETTING);
        redisTemplate.delete(RedisConstants.IS_IMPORT);
        return ResultBody.success();
    }

    @ApiOperation("判断是否完成导入")
    @PostMapping("/teacher/admission/isImport")
    public ResultBody isImport() {
        return ResultBody.success(redisTemplate.opsForValue().get(RedisConstants.IS_IMPORT) != null);
    }

    @ApiOperation("判断是否完成专业设置")
    @PostMapping("/teacher/admission/isPlanSetting")
    public ResultBody isPlanSetting() {
        return ResultBody.success(redisTemplate.opsForValue().get(RedisConstants.IS_PLAN_SETTING) != null);
    }

    @ApiOperation("判断是否完成志愿填报时间段设置")
    @PostMapping("/teacher/admission/isVolunteerTimeSetting")
    public ResultBody isVolunteerTimeSetting() {
        return ResultBody.success(redisTemplate.opsForValue().get(RedisConstants.IS_VOLUNTEER_TIME_SETTING) != null);
    }

    @ApiOperation("判断是否执行录取")
    @PostMapping("/teacher/admission/isExecAdm")
    public ResultBody isExecAdm() {
        return ResultBody.success(redisTemplate.opsForValue().get(RedisConstants.IS_EXEC_ADMISSION) != null);
    }

    @ApiOperation("判断是否发布录取")
    @PostMapping("/teacher/admission/isPublishAdm")
    public ResultBody isPublishAdm() {
        return ResultBody.success(redisTemplate.opsForValue().get(RedisConstants.IS_PUBLISH_ADMISSION) != null);
    }
}

package com.glxy.pro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.glxy.pro.DTO.PageDTO;
import com.glxy.pro.bo.AdmissionBo;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.bo.VolunteerBo;
import com.glxy.pro.common.CommonEnum;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.Admission;
import com.glxy.pro.entity.DivisionResult;
import com.glxy.pro.mapper.AdmissionMapper;
import com.glxy.pro.query.AdmissionQuery;
import com.glxy.pro.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.glxy.pro.constant.RedisConstants.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Service
public class AdmissionServiceImpl extends ServiceImpl<AdmissionMapper, Admission> implements IAdmissionService {
    @Autowired
    private AdmissionMapper admissionMapper;

    @Autowired
    private IAdmissionService admissionService;

    @Autowired
    private IDivisionResultService divisionResultService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private IMajorService majorService;

    @Autowired
    private IVolunteerService volunteerService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public List<AdmissionBo> getAdmissionByYearAndCate(String categoryName, Integer admYear) {
        // 查缓存
//        List<AdmissionBo> res = redisTemplate.opsForList().range(ADM_CACHE + admYear, 0, -1);
//        if (res != null && !res.isEmpty()) {
            // 缓存命中 直接返回
//            return res;
//        }else{
//            // 缓存未命中 查数据库
//            res = admissionMapper.getAdmissionByYearAndCate(categoryName, admYear);
//            if (!res.isEmpty()) {
//                // 查到了 构建缓存
//                redisTemplate.opsForList().rightPushAll(ADM_CACHE + admYear, res);
//                redisTemplate.expire(ADM_CACHE + admYear, TWELVE_HOUR_TTL, TimeUnit.SECONDS);
//                return res;
//            }
//        }
        return admissionMapper.getAdmissionByYearAndCate(categoryName, admYear);
    }

    @Override
    public CommonEnum setEnrollmentPlan(AdmissionBo admissionBo) {
        Admission admission = new Admission();
        BeanUtils.copyProperties(admissionBo, admission);
        LambdaQueryWrapper<Admission> wrapper = new LambdaQueryWrapper<Admission>()
                .eq(Admission::getAdmYear, admission.getAdmYear())
                .eq(Admission::getMajorId, admission.getMajorId());
        if(admissionMapper.selectOne(wrapper) != null) {
            return updateEnrollmentPlan(admissionBo);
        } else {
            admissionMapper.insert(admission);
            return CommonEnum.ADD_ADM_SUCCESS;
        }
    }

    @Override
    public CommonEnum updateEnrollmentPlan(AdmissionBo admissionBo) {
        Admission admission = new Admission();
        BeanUtils.copyProperties(admissionBo, admission);
        LambdaQueryWrapper<Admission> wrapper = new LambdaQueryWrapper<Admission>()
                .eq(Admission::getAdmYear, admission.getAdmYear())
                .eq(Admission::getMajorId, admission.getMajorId());
        admissionMapper.update(admission, wrapper);
        return CommonEnum.UPDATE_ADM_SUCCESS;
    }

    @Override
    public PageDTO<AdmissionBo> queryAdmissionPage(AdmissionQuery query) {
        // 1.构建条件
        Page<AdmissionBo> page = query.toMpPageDefaultSort("adm_year");
        page.setSearchCount(true);
        // 2.查询
        Integer begin = (query.getPageNo() - 1) * query.getPageSize();
        List<AdmissionBo> admissionBos = admissionMapper.queryAdmissionPage(query, begin);
        // 2.1 查询总数
        Integer total = admissionMapper.queryAdmissionCount(query);
        page.setRecords(admissionBos);
        page.setTotal(total);
        // 总页数
        int totalPage = total % query.getPageSize() == 0 ? total / query.getPageSize() : total / query.getPageSize() + 1;
        page.setPages(totalPage);
        return PageDTO.of(page, AdmissionBo.class);
    }

    @Override
    public List<AdmissionBo> getNoFullAdmission(String categoryId, int lib, int year) {
        return admissionMapper.getNoFullAdmission(categoryId, lib, year);
    }

    @Override
    public List<AdmissionBo> getAdmissionByCategoryId(String categoryId, int year) {
        return admissionMapper.getAdmissionByCategoryId(categoryId, year);
    }

    public boolean resetAllAdmissionNum(Integer year) {
        return admissionMapper.resetAllAdmissionNum(year);
    }

    public List<Integer> getNowStuCount(Integer year, String majorId) {
        Admission admission = admissionService.lambdaQuery()
                .eq(Admission::getAdmYear, year)
                .eq(Admission::getMajorId, majorId)
                .one();
        List<Integer> nowStuCount = new ArrayList<>();
        nowStuCount.add(admission.getNowScienceStuCount());
        nowStuCount.add(admission.getNowHumanitiesStuCount());
        return nowStuCount;
    }

    public void addNowStuCount(Integer year, String majorId, Integer wOrl) {
        if(wOrl == 0){
            admissionMapper.addSciNowStuCount(year, majorId);
        }else if (wOrl == 1){
            admissionMapper.addHumNowStuCount(year, majorId);
        }
    }

    public List<Integer> getLowOrHighRanking(Integer year, String majorId, Integer wOrl){
        Admission admission = admissionService.lambdaQuery()
                .eq(Admission::getAdmYear, year)
                .eq(Admission::getMajorId, majorId)
                .one();
        List<Integer> LowOrHighRanking = new ArrayList<>();
        if (admission != null) {
            if (wOrl == 0) {
                LowOrHighRanking.add(admission.getScienceHigh());
                LowOrHighRanking.add(admission.getScienceLow());
            } else {
                LowOrHighRanking.add(admission.getHumanitiesHigh());
                LowOrHighRanking.add(admission.getHumanitiesLow());
            }
        }
        return LowOrHighRanking;
    }

    public void UpdateLowOrHighRanking(Integer year, String majorId, Integer wOrl, Integer ranking, Boolean flag){
        if (flag) {
            // 更新最高排名
            if(wOrl == 0){
                // 理科最高
                admissionService.lambdaUpdate()
                        .eq(Admission::getAdmYear, year)
                        .eq(Admission::getMajorId, majorId)
                        .set(Admission::getScienceHigh, ranking)
                        .update();
            }else if (wOrl == 1) {
                // 文科最高
                admissionService.lambdaUpdate()
                        .eq(Admission::getAdmYear, year)
                        .eq(Admission::getMajorId, majorId)
                        .set(Admission::getHumanitiesHigh, ranking)
                        .update();
            }
        }else {
            // 更新最低排名
            if(wOrl == 0){
                // 理科最低
                admissionService.lambdaUpdate()
                        .eq(Admission::getAdmYear, year)
                        .eq(Admission::getMajorId, majorId)
                        .set(Admission::getScienceLow, ranking)
                        .update();
            }else if (wOrl == 1) {
                // 文科最低
                admissionService.lambdaUpdate()
                        .eq(Admission::getAdmYear, year)
                        .eq(Admission::getMajorId, majorId)
                        .set(Admission::getHumanitiesLow, ranking)
                        .update();
            }
        }
    }

    @Override
    public ResultBody autoAdmission(Integer year) {
        // 重置所有招生计划的最高、最低排名、已录取人数为零
        if(resetAllAdmissionNum(year) && divisionResultService.resetAllMajor(year - 1)) {
            // 获取全部大类ID集合
            List<String> categoryIds = categoryService.allCategroyIds();
            // 遍历集合，每遍历一次进行一个大类的录取
            for (String categoryId : categoryIds) {
                // 用这个专业ID集合和当前年份获取该大类的所有招生计划
                List<AdmissionBo> admissionBoList = admissionMapper.getAdmissionByYearAnCategoryId(year, categoryId);
                if(admissionBoList.size() == 0) {
                    // 说明该大类下没有招生计划
                    continue;
                }
                // 现在进行该大类下的学生录取，遍历两次，分别进行理/文科的录取
                for (int i = 0; i < 2; i++) {
                    // 再根据这些ID集合去获取这些学生的志愿数据，按学号聚类好
                    List<VolunteerBo> volunteerBoList = volunteerService.getByCategoryIdAndLib(categoryId, i, year-1);
                    if(volunteerBoList.size() == 0) {
                        // 说明这个大类下没有学生填志愿
                        continue;
                    }
                    Map<String, List<VolunteerBo>> groupedVolunteers = volunteerBoList.stream()
                            .collect(Collectors.groupingBy(VolunteerBo::getStuId));

                    // 获取录取结果（只有排名，还没有最终录入专业的）集合
                    List<DivisionResultBo> divisionResultBoList = divisionResultService.getDivisionResultByCategoryIdAndLib(categoryId, i, year-1);
                    // 根据排名排序
                    divisionResultBoList.sort(Comparator.comparing(DivisionResultBo::getRanking));

                    // 进行按序录取
                    outerLoop:
                    for (DivisionResultBo divisionResultBo : divisionResultBoList) {
                        // 该排名下的学生学号
                        String stuId = divisionResultBo.getStuId();
                        // 该学生的所有志愿的集合
                        List<VolunteerBo> volunteerBos = groupedVolunteers.get(stuId);
                        if(volunteerBos == null) {
                            // 说明这个小可爱没填志愿
                            continue;
                        }
                        // 按照第几志愿为这个集合排序
                        volunteerBos.sort(Comparator.comparing(VolunteerBo::getWhich));
                        // 从第一志愿开始，遍历该集合
                        innerLoop:
                        for (VolunteerBo volunteerBo : volunteerBos) {
                            // 循环该大类下的所有招生计划
                            for (AdmissionBo admissionBo : admissionBoList) {
                                // 判断该招生计划的所属专业是否和该学生志愿的专业相同
                                if(Objects.equals(admissionBo.getMajorId(), volunteerBo.getMajorId())){
                                    // 获取当前专业已录取人数集合，集合包括两个元素，第一个是理科已录取人数，第二个是文科已录取人数
                                    List<Integer> nowStuCount = getNowStuCount(year, admissionBo.getMajorId());
                                    // 获取当前分科的计划录取人数
                                    int planCount = i == 0 ? admissionBo.getScienceStuCount() : admissionBo.getHumanitiesStuCount();
                                    // 判断是否满员
                                    if(nowStuCount.get(i) < planCount) {
                                        //说明人数没满，可以录入
                                        // 把该学生录进该专业
                                        divisionResultService.admissionOne(admissionBo.getMajorId(), stuId);
                                        // 该专业该分科已录取人数+1
                                        addNowStuCount(year, admissionBo.getMajorId(), i);
                                        // 初始化最高排名
                                        List<Integer> maxAndminRanking = getLowOrHighRanking(year, admissionBo.getMajorId(), i);
                                        if(maxAndminRanking.get(0) == 0){
                                            maxAndminRanking.set(0, Integer.MAX_VALUE);
                                        }
                                        // 更新该招生计划的最低和最高排名
                                        // 判断该学生排名是否比上一位成功录取学生的排名低，若通过，则更新最低排名
                                        if(divisionResultBo.getRanking() > maxAndminRanking.get(1)){
                                            // false表示当前要改的是最低排名
                                            UpdateLowOrHighRanking(year, admissionBo.getMajorId(), i, divisionResultBo.getRanking(), false);
                                        }
                                        // 判断该学生排名是否比上一位成功录取学生的排名高，若通过，则更新最高排名
                                        if(divisionResultBo.getRanking() < maxAndminRanking.get(0)){
                                            // true表示当前要更新的是最高排名
                                            UpdateLowOrHighRanking(year, admissionBo.getMajorId(), i, divisionResultBo.getRanking(), true);
                                        }
                                        // 录取成功后跳出志愿循环，进入下一个学生的录取
                                        break innerLoop;
                                    }else {
                                        // 说明人数满了，进入下一个志愿
                                        break;
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        return ResultBody.success();
    }

    @Override
    public ResultBody supplementation(Integer year) {
        // 获取全部大类ID集合
        List<String> categoryIds = categoryService.allCategroyIds();
        for (String categoryId : categoryIds) {
            // 现在进行该大类下的学生补录，遍历两次，分别进行理/文科的补录
            for (int i = 0; i < 2; i++) {
                List<DivisionResultBo> divisionResultBoList = divisionResultService.getAllNoMajorStudent(categoryId, i, year-1);
                if(divisionResultBoList.size() == 0) {
                    // 说明这个大类没有需要补录的学生
                    continue;
                }
                // 打乱集合顺序，随机补录
                Collections.shuffle(divisionResultBoList);
                // 获取该大类、该分科下，还没满员可以进行录取的招生计划
                List<AdmissionBo> admissionBoList = admissionService.getNoFullAdmission(categoryId, i, year);
                if(admissionBoList.size() == 0) {
                    // 说明是天选打工人，进厂吧
                    continue;
                }
                for (DivisionResultBo divisionResultBo : divisionResultBoList) {
                    for (AdmissionBo admissionBo : admissionBoList) {
                        List<Integer> nowStuCount = getNowStuCount(year, admissionBo.getMajorId());
                        // 获取当前分科的计划录取人数
                        int planCount = i == 0 ? admissionBo.getScienceStuCount() : admissionBo.getHumanitiesStuCount();
                        if(nowStuCount.get(i) < planCount) {
                            //说明人数没满，可以录入
                            // 把该学生录进该专业
                            divisionResultService.admissionOne(admissionBo.getMajorId(), divisionResultBo.getStuId());
                            // 该专业该分科已录取人数+1
                            addNowStuCount(year, admissionBo.getMajorId(), i);
                            // 初始化最高排名
                            List<Integer> maxAndminRanking = getLowOrHighRanking(year, admissionBo.getMajorId(), i);
                            if(maxAndminRanking.get(0) == 0){
                                maxAndminRanking.set(0, Integer.MAX_VALUE);
                            }
                            // 更新该招生计划的最低和最高排名
                            // 判断该学生排名是否比目前的最低排名低，若通过，则更新最低排名
                            if(divisionResultBo.getRanking() > maxAndminRanking.get(1)){
                                // false表示当前要改的是最低排名
                                UpdateLowOrHighRanking(year, admissionBo.getMajorId(), i, divisionResultBo.getRanking(), false);
                            }
                            // 判断该学生排名是否比目前的最高排名高，若通过，则更新最高排名
                            if(divisionResultBo.getRanking() < maxAndminRanking.get(0)){
                                // true表示当前要更新的是最高排名
                                UpdateLowOrHighRanking(year, admissionBo.getMajorId(), i, divisionResultBo.getRanking(), true);
                            }
                            // 录取成功后跳出循环，进入下一个学生的补录
                            break;
                        }
                    }
                }
            }
            // 获取还没满员的专业
        }
        return ResultBody.success();
    }
}

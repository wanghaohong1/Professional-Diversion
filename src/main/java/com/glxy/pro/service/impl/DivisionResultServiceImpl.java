package com.glxy.pro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.glxy.pro.DTO.PageDTO;
import com.glxy.pro.bo.AdmissionBo;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.common.CommonEnum;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.DivisionResult;
import com.glxy.pro.mapper.DivisionResultMapper;
import com.glxy.pro.query.DivisionResultQuery;
import com.glxy.pro.service.IDivisionResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.glxy.pro.constant.RedisConstants.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Service
public class DivisionResultServiceImpl extends ServiceImpl<DivisionResultMapper, DivisionResult> implements IDivisionResultService {

    @Autowired
    private DivisionResultMapper divisionResultMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public DivisionResultBo getDivisionResultById(String stuId) {
        // 先查缓存
        if (Boolean.TRUE.equals(redisTemplate.hasKey(DIVISION_CACHE + stuId))) {
            // 缓存命中 直接返回
            return (DivisionResultBo) redisTemplate.opsForValue().get(DIVISION_CACHE + stuId);
        }else{
            // 缓存未命中 查数据库
            DivisionResultBo res = divisionResultMapper.selectDivisionResultById(stuId);
            if (res != null) {
                // 数据库命中 构建缓存
                redisTemplate.opsForValue().set(DIVISION_CACHE + stuId, res);
                redisTemplate.expire(DIVISION_CACHE + stuId, TWELVE_HOUR_TTL, TimeUnit.SECONDS);
                return res;
            }else {
                // 数据库未命中 返回空
                return null;
            }
        }
    }

    @Override
    public ResultBody saveDivisionResult(List<DivisionResultBo> divisionResultBoList) {
        //返回值
        if (divisionResultBoList.size() == 0) {
            return ResultBody.error(CommonEnum.NO_INFO);
        } else {
            List<DivisionResult> divisionResultList = new ArrayList<>();
            for (DivisionResultBo divisionResultBo : divisionResultBoList) {
                DivisionResult divisionResult = new DivisionResult();
                BeanUtils.copyProperties(divisionResultBo, divisionResult);
                divisionResultList.add(divisionResult);
            }
            saveBatch(divisionResultList);
            return ResultBody.success(CommonEnum.SUCCESS);
        }
    }

    @Override
    public PageDTO<DivisionResultBo> queryDivisionResultPage(DivisionResultQuery query) {
        // 1.构建条件
        Page<DivisionResultBo> page = query.toMpPageDefaultSort("stu_id");
        page.setSearchCount(true);
        // 2.查询
        Integer begin = (query.getPageNo() - 1) * query.getPageSize();
        List<DivisionResultBo> divisionResultBos = divisionResultMapper.queryDivisionResultPage(query, begin);
        // 2.1 查询总数
        Integer total = divisionResultMapper.queryDivisionResultCount(query);
        page.setRecords(divisionResultBos);
        page.setTotal(total);
        // 总页数
        int totalPage = total % query.getPageSize() == 0 ? total / query.getPageSize() : total / query.getPageSize() + 1;
        page.setPages(totalPage);
        return PageDTO.of(page, DivisionResultBo.class);
    }

    @Override
    public List<DivisionResultBo> getAllNoMajorStudent(String categoryId, Integer lib, Integer grade) {
        return divisionResultMapper.getAllNoMajorStudent(categoryId, lib, grade);
    }

    @Override
    public boolean resetAllMajor(Integer grade) {
        return divisionResultMapper.resetAllMajor(grade);
    }

    @Override
    public List<DivisionResultBo> getDivisionResultByCategoryIdAndLib(String categoryId, Integer lib, Integer grade) {
        return divisionResultMapper.getDivisionResultByCategoryIdAndLib(categoryId, lib, grade);
    }

    @Override
    public void admissionOne(String majorId, String stuId) {
//        if (divisionResultService.lambdaUpdate()
//                .set(DivisionResult::getMajorId, majorId)
//                .eq(DivisionResult::getStuId, stuId)
//                .update()) {
//            return ResultBody.success();
//        } else {
//            return ResultBody.error("专业录取失败");
//        }
        divisionResultMapper.admissionOne(majorId, stuId);
    }
}

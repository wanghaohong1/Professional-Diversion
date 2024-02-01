package com.glxy.pro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.common.CommonEnum;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.DivisionResult;
import com.glxy.pro.mapper.DivisionResultMapper;
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
}

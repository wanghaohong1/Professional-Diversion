package com.glxy.pro.service.impl;

import com.glxy.pro.bo.AdmissionBo;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.bo.VolunteerBo;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.Admission;
import com.glxy.pro.entity.DivisionResult;
import com.glxy.pro.mapper.AdmissionMapper;
import com.glxy.pro.service.IAdmissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private RedisTemplate redisTemplate;


    @Override
    public List<AdmissionBo> getAdmissionByYear(String categoryName, Integer admYear) {
        // 查缓存
        List<AdmissionBo> res = redisTemplate.opsForList().range(ADM_CACHE + admYear, 0, -1);
        if (res != null && !res.isEmpty()) {
            // 缓存命中 直接返回
            return res;
        }else{
            // 缓存未命中 查数据库
            res = admissionMapper.getAdmissionByYear(categoryName, admYear);
            if (!res.isEmpty()) {
                // 查到了 构建缓存
                redisTemplate.opsForList().rightPushAll(ADM_CACHE + admYear, res);
                redisTemplate.expire(ADM_CACHE + admYear, TWELVE_HOUR_TTL, TimeUnit.SECONDS);
                return res;
            }
        }
        // 查不到 返回空
        return null;
    }
}

package com.glxy.pro.service.impl;

import com.glxy.pro.constant.RedisConstants;
import com.glxy.pro.entity.Gaokao;
import com.glxy.pro.mapper.GaokaoMapper;
import com.glxy.pro.service.IGaokaoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.glxy.pro.constant.RedisConstants.GAOKAO_CACHE;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Service
public class GaokaoServiceImpl extends ServiceImpl<GaokaoMapper, Gaokao> implements IGaokaoService {
    @Autowired
    private GaokaoMapper gaokaoMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Gaokao getGaokaoById(String stuId) {
        Gaokao gaokao = (Gaokao) redisTemplate.opsForValue().get(GAOKAO_CACHE + stuId);
        if (gaokao == null) {
            gaokao = getById(stuId);
            if (gaokao == null) {
                return null;
            } else {
                redisTemplate.opsForValue().set(GAOKAO_CACHE + stuId, gaokao, RedisConstants.HALF_HOUR_TTL, TimeUnit.SECONDS);
            }
        }
        return gaokao;
    }

    @Override
    public List<Gaokao> getGaokaoByGrade(Integer grade) {
        return gaokaoMapper.getGaokaoByGrade(grade);
    }

    @Override
    public void removeGaokaoByGrade(Integer grade) {
        gaokaoMapper.removeGaokaoByGrade(grade);
    }
}

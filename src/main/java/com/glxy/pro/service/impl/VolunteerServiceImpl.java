package com.glxy.pro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.glxy.pro.DTO.PageDTO;
import com.glxy.pro.DTO.VolunteerDTO;
import com.glxy.pro.bo.StudentBo;
import com.glxy.pro.bo.VolunteerBo;
import com.glxy.pro.entity.Volunteer;
import com.glxy.pro.mapper.VolunteerMapper;
import com.glxy.pro.query.StudentQuery;
import com.glxy.pro.query.VolunteerQuery;
import com.glxy.pro.service.IVolunteerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.glxy.pro.constant.RedisConstants.TWELVE_HOUR_TTL;
import static com.glxy.pro.constant.RedisConstants.VOLUNTEER_CACHE;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Service
public class VolunteerServiceImpl extends ServiceImpl<VolunteerMapper, Volunteer> implements IVolunteerService {
    @Autowired
    private VolunteerMapper volunteerMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<VolunteerBo> getVolunteerById(String stuId) {
        // 查缓存
        List<VolunteerBo> res = redisTemplate.opsForList().range(VOLUNTEER_CACHE + stuId, 0, -1);
        if (res != null && !res.isEmpty()) {
            // 缓存命中 直接返回
            return res;
        }else{
            // 缓存未命中 查数据库
            res = volunteerMapper.getVolunteerById(stuId);
            if (!res.isEmpty()) {
                // 查到了 构建缓存
                redisTemplate.opsForList().rightPushAll(VOLUNTEER_CACHE + stuId, res);
                redisTemplate.expire(VOLUNTEER_CACHE + stuId, TWELVE_HOUR_TTL, TimeUnit.SECONDS);
                return res;
            }
        }
        // 查不到 返回空
        return null;
    }

    @Override
    public boolean resetVolunteers(String stuId) {
        LambdaQueryWrapper<Volunteer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Volunteer::getStuId, stuId);
        // 先删缓存
        redisTemplate.delete(VOLUNTEER_CACHE + stuId);
        // 再删数据库
        return remove(wrapper);
    }

    @Override
    public void removeBatchByStuIds(List<String> ids) {
        volunteerMapper.removeBatchByStuIds(ids);
    }

    public List<StudentBo> getUnFillStuByCateId(String cateId) {
        return volunteerMapper.getUnFillStuByCateId(cateId);
    }

    @Override
    public int getFillCountByCateId(String cateId) {
        return volunteerMapper.getFillCountByCateId(cateId);
    }

    @Override
    public PageDTO<VolunteerDTO> getVolunteerByPagesAndConditions(StudentQuery query) {
        return volunteerMapper.getVolunteerByPagesAndConditions(query);
    }

}

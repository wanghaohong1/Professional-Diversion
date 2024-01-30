package com.glxy.pro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.glxy.pro.entity.FreshmanGrades;
import com.glxy.pro.mapper.FreshmanGradesMapper;
import com.glxy.pro.service.IFreshmanGradesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.glxy.pro.constant.RedisConstants.FRESHMAN_GRADES_CACHE;
import static com.glxy.pro.constant.RedisConstants.TWELVE_HOUR_TTL;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Service
public class FreshmanGradesServiceImpl extends ServiceImpl<FreshmanGradesMapper, FreshmanGrades> implements IFreshmanGradesService {
    @Autowired
    private FreshmanGradesMapper freshmanGradesMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<FreshmanGrades> getFreshmanGradesById(String stuId) {
        // 查缓存
        List<FreshmanGrades> stuGradesList = redisTemplate.opsForList().range(FRESHMAN_GRADES_CACHE + stuId, 0, -1);
        if (stuGradesList.isEmpty()) {
            // 查数据库
            LambdaQueryWrapper<FreshmanGrades> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FreshmanGrades::getStuId, stuId);
            stuGradesList = list(wrapper);
            if (stuGradesList.isEmpty()) {
                return null;
            } else {
                // 存入redis
                redisTemplate.opsForList().rightPushAll(FRESHMAN_GRADES_CACHE + stuId, stuGradesList);
                redisTemplate.expire(FRESHMAN_GRADES_CACHE + stuId, TWELVE_HOUR_TTL, TimeUnit.SECONDS);
            }
        }
        return stuGradesList;
    }

    @Override
    public void removeFreshmanGradesByGrade(Integer grade) {
        freshmanGradesMapper.removeFreshmanGradesByGrade(grade);
    }

    @Override
    public boolean saveFreshmanGradesBatch(List<FreshmanGrades> importFreshmanGrades) {
        Set<String> primarySet = new HashSet<>();
        List<FreshmanGrades> cleanImport = new ArrayList<>();
        for (FreshmanGrades freshmanGrades : importFreshmanGrades) {
            String pK = freshmanGrades.getStuId() + "-" + freshmanGrades.getCourseName();
            if (primarySet.contains(pK)) {
                continue;
            }
            primarySet.add(pK);
            cleanImport.add(freshmanGrades);
        }
        return saveBatch(cleanImport);
    }

    @Override
    public void removeBatchByStuIds(List<String> ids) {
        freshmanGradesMapper.removeBatchByStuIds(ids);
    }
}

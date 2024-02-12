package com.glxy.pro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.Major;
import com.glxy.pro.mapper.MajorMapper;
import com.glxy.pro.service.IMajorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements IMajorService {

    @Autowired
    private MajorMapper majorMapper;


    @Override
    public List<Major> getMajorByCategoryName(String categoryName) {
        return majorMapper.getMajorByCategoryName(categoryName);
    }

    @Override
    public List<Major> getMajorByCategoryId(String categoryId) {
        return lambdaQuery().eq(Major::getCategoryId, categoryId).list();
    }

    @Override
    public List<String> getMajorIdsByCategoryId(String categoryId){
        LambdaQueryWrapper<Major> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Major::getCategoryId, categoryId);
        List<Major> majorList = list(wrapper);
        return majorList.stream()
                .map(Major::getMajorId)
                .collect(Collectors.toList());
    }
}

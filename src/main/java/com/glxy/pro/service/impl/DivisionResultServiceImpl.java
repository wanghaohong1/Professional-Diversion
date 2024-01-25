package com.glxy.pro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.entity.DivisionResult;
import com.glxy.pro.mapper.DivisionResultMapper;
import com.glxy.pro.service.IDivisionResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private DivisionResultMapper mapper;

    @Override
    public DivisionResultBo getDivisionResultById(String stuId) {
        return mapper.selectDivisionResultById(stuId);
    }
}

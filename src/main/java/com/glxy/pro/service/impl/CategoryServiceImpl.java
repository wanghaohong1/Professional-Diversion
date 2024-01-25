package com.glxy.pro.service.impl;

import com.glxy.pro.entity.Category;
import com.glxy.pro.mapper.CategoryMapper;
import com.glxy.pro.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public Category getCategoryById(String categoryId) {
        return getById(categoryId);
    }
}

package com.glxy.pro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.glxy.pro.entity.Category;
import com.glxy.pro.mapper.CategoryMapper;
import com.glxy.pro.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public boolean saveOrUpdateCategory(String categoryName, Integer stuNum) {
        // 判断该大类是否存在
        List<Category> list = lambdaQuery()
                .eq(Category::getCategoryName, categoryName)
                .list();
        Category category = new Category();
        if (list.size() != 0) {
            // 如果存在 将原来的id赋值给category
            category.setCategoryId(list.get(0).getCategoryId());
        } else {
            // 如果不存在 生成一个新的id
            category.setCategoryId(UUID.randomUUID().toString());
        }
        category.setCategoryName(categoryName);
        category.setStuNum(stuNum);
        return saveOrUpdate(category);

    }

    @Override
    public Category getCategoryByName(String categoryName) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getCategoryName, categoryName);
        return getOne(wrapper);
    }
}

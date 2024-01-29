package com.glxy.pro.service;

import com.glxy.pro.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
public interface ICategoryService extends IService<Category> {

    boolean saveOrUpdateCategory(String categoryName, Integer stuNum);

    Category getCategoryByName(String categoryName);

}

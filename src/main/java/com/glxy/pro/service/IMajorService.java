package com.glxy.pro.service;

import com.glxy.pro.entity.Major;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
public interface IMajorService extends IService<Major> {

    List<Major> getMajorByCategoryName(String categoryName);


    List<Major> getMajorByCategoryId(String categoryId);

    List<String> getMajorIdsByCategoryId(String categoryId);
}

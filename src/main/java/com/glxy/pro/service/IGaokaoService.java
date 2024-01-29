package com.glxy.pro.service;

import com.glxy.pro.entity.Gaokao;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
public interface IGaokaoService extends IService<Gaokao> {

    Gaokao getGaokaoById(String stuId);

    List<Gaokao> getGaokaoByGrade(Integer grade);

    void removeGaokaoByGrade(Integer grade);
}

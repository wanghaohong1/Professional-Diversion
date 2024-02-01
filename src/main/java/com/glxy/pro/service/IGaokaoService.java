package com.glxy.pro.service;

import com.glxy.pro.bo.GaokaoBo;
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

    /**
     * 修改学生高考信息
     *
     * @param gaokao 待修改的高考信息（包含学号）
     * @return 是否成功
     */
    boolean updateGaokao(Gaokao gaokao);
}

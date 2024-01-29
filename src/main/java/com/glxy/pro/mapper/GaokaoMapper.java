package com.glxy.pro.mapper;

import com.glxy.pro.entity.Gaokao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Mapper
public interface GaokaoMapper extends BaseMapper<Gaokao> {

    List<Gaokao> getGaokaoByGrade(Integer grade);

    void removeGaokaoByGrade(Integer grade);
}

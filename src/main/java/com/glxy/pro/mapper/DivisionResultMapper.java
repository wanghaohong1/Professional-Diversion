package com.glxy.pro.mapper;

import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.entity.DivisionResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Mapper
public interface DivisionResultMapper extends BaseMapper<DivisionResult> {

    DivisionResultBo selectDivisionResultById(@Param("stuId") String stuId);

}

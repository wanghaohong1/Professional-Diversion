package com.glxy.pro.mapper;

import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.entity.DivisionResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glxy.pro.query.DivisionResultQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface DivisionResultMapper extends BaseMapper<DivisionResult> {

    DivisionResultBo selectDivisionResultById(String stuId);

    List<DivisionResultBo> queryDivisionResultPage(DivisionResultQuery query, Integer begin);

    Integer queryDivisionResultCount(DivisionResultQuery query);

    List<DivisionResultBo> getAllNoMajorStudent(@Param("categoryId") String categoryId, @Param("lib") Integer lib, @Param("grade") Integer grade);

    boolean resetAllMajor(Integer grade);

    List<DivisionResultBo> getDivisionResultByCategoryIdAndLib(String categoryId, Integer lib, Integer grade);

    boolean admissionOne(String majorId, String stuId);
}

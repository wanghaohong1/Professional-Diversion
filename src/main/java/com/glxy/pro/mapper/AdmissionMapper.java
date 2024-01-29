package com.glxy.pro.mapper;

import com.glxy.pro.bo.AdmissionBo;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.entity.Admission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
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
public interface AdmissionMapper extends BaseMapper<Admission> {

    List<AdmissionBo> getAdmissionByYear(String categoryName, Integer admYear);

}

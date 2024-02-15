package com.glxy.pro.mapper;

import com.glxy.pro.bo.AdmissionBo;
import com.glxy.pro.entity.Admission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glxy.pro.query.AdmissionQuery;
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

    List<AdmissionBo> getAdmissionByYearAndCate(String categoryName, Integer admYear);

    List<AdmissionBo> queryAdmissionPage(AdmissionQuery query, Integer begin);

    Integer queryAdmissionCount(AdmissionQuery query);

    List<AdmissionBo> getNoFullAdmission(String categoryId, int lib, Integer year);

    List<AdmissionBo> getAdmissionByCategoryId(String categoryId, Integer year);

    boolean addHumNowStuCount(Integer year, String majorId);

    boolean addSciNowStuCount(Integer year, String majorId);

    boolean resetAllAdmissionNum(Integer year);

    List<AdmissionBo> getAdmissionByYearAnCategoryId(Integer year, String categoryId);

    List<AdmissionBo> getAdmissionGroupByStuId(String stuId);
}

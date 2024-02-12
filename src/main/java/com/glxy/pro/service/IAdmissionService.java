package com.glxy.pro.service;

import com.glxy.pro.DTO.PageDTO;
import com.glxy.pro.bo.AdmissionBo;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.common.CommonEnum;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.Admission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.glxy.pro.entity.DivisionResult;
import com.glxy.pro.query.AdmissionQuery;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
public interface IAdmissionService extends IService<Admission> {

    List<AdmissionBo> getAdmissionByYearAndCate(String categoryName, Integer admYear);

    CommonEnum setEnrollmentPlan(AdmissionBo admissionBo);

    CommonEnum updateEnrollmentPlan(AdmissionBo admissionBo);

    PageDTO<AdmissionBo> queryAdmissionPage(AdmissionQuery query);

    List<AdmissionBo> getNoFullAdmission(String categoryId, int lib, int year);

    List<AdmissionBo> getAdmissionByCategoryId(String categoryId, int year);

    ResultBody autoAdmission(Integer year);

    ResultBody supplementation(Integer year);
}

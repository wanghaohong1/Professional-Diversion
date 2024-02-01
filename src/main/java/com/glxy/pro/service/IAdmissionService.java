package com.glxy.pro.service;

import com.glxy.pro.bo.AdmissionBo;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.Admission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.glxy.pro.entity.DivisionResult;

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


}

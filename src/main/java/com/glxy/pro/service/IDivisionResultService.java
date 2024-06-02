package com.glxy.pro.service;

import com.glxy.pro.dto.PageDto;
import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.DivisionResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.glxy.pro.query.DivisionResultQuery;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
public interface IDivisionResultService extends IService<DivisionResult> {

    DivisionResultBo getDivisionResultById(String stuId);

    ResultBody saveDivisionResult(@RequestBody List<DivisionResultBo> divisionResultBoList);

    PageDto<DivisionResultBo> queryDivisionResultPage(DivisionResultQuery query);

    List<DivisionResultBo> getAllNoMajorStudent(String categoryId, Integer lib, Integer grade);

    boolean resetAllMajor(Integer grade);

    List<DivisionResultBo> getDivisionResultByCategoryIdAndLib(String categoryId, Integer lib, Integer grade);

    void admissionOne(String majorId, String stuId);
}

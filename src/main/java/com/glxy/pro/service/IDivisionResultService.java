package com.glxy.pro.service;

import com.glxy.pro.bo.DivisionResultBo;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.DivisionResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
}

package com.glxy.pro.service;

import com.glxy.pro.bo.VolunteerBo;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.Volunteer;
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
public interface IVolunteerService extends IService<Volunteer> {

    List<VolunteerBo> getVolunteerById(String stuId);

    boolean resetVolunteers(String stuId);

    void removeBatchByStuIds(List<String>ids);

}

package com.glxy.pro.service;

import com.glxy.pro.DTO.PageDTO;
import com.glxy.pro.DTO.VolunteerDTO;
import com.glxy.pro.bo.StudentBo;
import com.glxy.pro.bo.VolunteerBo;
import com.glxy.pro.entity.Volunteer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.glxy.pro.query.StudentQuery;
import com.glxy.pro.query.VolunteerQuery;

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
    List<StudentBo> getUnFillStuByCateId(String cateId);

    int getFillCountByCateId(String cateId);

    PageDTO<VolunteerDTO> getVolunteerByPagesAndConditions(StudentQuery query);
}

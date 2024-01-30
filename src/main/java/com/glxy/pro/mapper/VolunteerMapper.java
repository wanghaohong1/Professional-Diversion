package com.glxy.pro.mapper;

import com.glxy.pro.DTO.PageDTO;
import com.glxy.pro.DTO.VolunteerDTO;
import com.glxy.pro.bo.StudentBo;
import com.glxy.pro.bo.VolunteerBo;
import com.glxy.pro.entity.Volunteer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glxy.pro.query.StudentQuery;
import org.apache.ibatis.annotations.Mapper;

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
public interface VolunteerMapper extends BaseMapper<Volunteer> {

    List<VolunteerBo> getVolunteerById(String stuId);

    List<StudentBo> getUnFillStuByCateId(String cateId);

    int getFillCountByCateId(String cateId);

    PageDTO<VolunteerDTO> getVolunteerByPagesAndConditions(StudentQuery query);
}

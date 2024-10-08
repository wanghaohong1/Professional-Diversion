package com.glxy.pro.mapper;

import com.glxy.pro.dto.PageDto;
import com.glxy.pro.dto.VolunteerDto;
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

    void removeBatchByStuIds(List<String>ids);

    List<StudentBo> getUnFillStuByCateId(String cateId, String stuName, Integer grade);

    int getFillCountByCateId(String cateId);

    List<VolunteerDto> getVolunteerByPagesAndConditions(StudentQuery query, Integer begin);

    Integer queryVolunteerCount(StudentQuery query);

    List<VolunteerBo> getByCategoryIdAndLib(String categoryId, int lib, Integer grade);
}

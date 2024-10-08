package com.glxy.pro.mapper;

import com.glxy.pro.dto.UserStudentDto;
import com.glxy.pro.bo.UserBo;
import com.glxy.pro.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.glxy.pro.query.StudentQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface UserMapper extends BaseMapper<User> {
    String getAdminEmail();

    User checkLogin(String userId, String password);

    int removeUsersByGrade(Integer grade);

    // 分页查询
    List<UserStudentDto> selectStudent(@Param("query") StudentQuery query, @Param("begin")int begin);

    // 分页查询获取总行数
    Integer selectStudentTotal(@Param("query")StudentQuery query);

}

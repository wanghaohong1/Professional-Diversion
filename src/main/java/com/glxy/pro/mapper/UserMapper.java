package com.glxy.pro.mapper;

import com.glxy.pro.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    User checkLogin(String userId, String password);

    int removeUsersByGrade(Integer grade);

}

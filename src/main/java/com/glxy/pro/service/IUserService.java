package com.glxy.pro.service;

import com.glxy.pro.bo.UserBo;
import com.glxy.pro.common.ResultBody;
import com.glxy.pro.entity.User;
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
public interface IUserService extends IService<User> {

    User getUserById(String userId);

    boolean checkLogin(UserBo loginMsg);

    String getUserIdByPhone(String phone);

    String getUserIdByEmail(String email);

    boolean updateUser(User user);

    String getStudentIdByPhone(String phone);

    String getStudentIdByEmail(String email);

    void removeUserByGrade(int grade);
}

package com.glxy.pro.service.impl;

import com.glxy.pro.entity.User;
import com.glxy.pro.mapper.UserMapper;
import com.glxy.pro.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}

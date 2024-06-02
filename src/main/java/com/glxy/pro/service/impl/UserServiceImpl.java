package com.glxy.pro.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.glxy.pro.dto.PageDto;
import com.glxy.pro.dto.UserStudentDto;
import com.glxy.pro.bo.UserBo;
import com.glxy.pro.entity.User;
import com.glxy.pro.mapper.UserMapper;
import com.glxy.pro.query.StudentQuery;
import com.glxy.pro.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.glxy.pro.constant.RedisConstants.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String getAdminEmail() {
        return userMapper.getAdminEmail();
    }

    @Override
    public User getUserById(String userId) {
        return getById(userId);
    }

    @Override
    public boolean checkLogin(UserBo loginMsg) {
        return userMapper.checkLogin(loginMsg.getId(), loginMsg.getPassword()) != null;
    }

    @Override
    public String getUserIdByPhone(String phone) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        User user = getOne(wrapper);
        return user != null ? user.getId() : null;
    }

    @Override
    public String getUserIdByEmail(String email) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        User user = getOne(wrapper);
        return user != null ? user.getId() : null;
    }

    @Override
    public boolean updateUser(User user) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        // 只更新不为空的字段
        updateWrapper.lambda()
                .eq(User::getId, user.getId())
                .set(user.getPassword() != null, User::getPassword, user.getPassword())
                .set(user.getPhone() != null, User::getPhone, user.getPhone())
                .set(user.getEmail() != null, User::getEmail, user.getEmail());
        boolean res = userMapper.update(user, updateWrapper) > 0;
        if(res) {
            redisTemplate.delete(USER_CACHE + user.getId());
            return true;
        }
        return false;
    }

    @Override
    public String getStudentIdByPhone(String phone) {
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        return user != null ? user.getId() : null;
    }

    @Override
    public String getStudentIdByEmail(String email) {
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        return user != null ? user.getId() : null;
    }

    @Override
    public void removeUserByGrade(int grade) {
        Set<String> keys = redisTemplate.keys(USER_CACHE + "*");
        redisTemplate.delete(keys);
        userMapper.removeUsersByGrade(grade);
    }

    @Override
    public PageDto<UserStudentDto> getUserStudentPage(StudentQuery studentQuery) {
        PageDto<UserStudentDto> pageDto = new PageDto<>();
        List<UserStudentDto> userStudentDtoS = userMapper.selectStudent(studentQuery, (studentQuery.getPageNo() - 1) * studentQuery.getPageSize());
        Integer total = userMapper.selectStudentTotal(studentQuery);
        pageDto.setPages((long) (total / studentQuery.getPageSize()) + 1);
        pageDto.setList(userStudentDtoS);
        pageDto.setTotal(Long.valueOf(total));
        return pageDto;
    }

    @Override
    public void removeCacheBatch(List<String> ids) {
        List<String> userCache = new ArrayList<>(ids.size());

        for (String id : ids) {
            userCache.add(USER_CACHE + id);
//            userCache.add(STUDENT_CACHE + id);
            userCache.add(FRESHMAN_GRADES_CACHE + id);
            userCache.add(GAOKAO_CACHE + id);
            userCache.add(VOLUNTEER_CACHE + id);
            userCache.add(DIVISION_CACHE + id);
            userCache.add(GRADE_LIST_CACHE + id);
            userCache.add(TOKEN_CACHE + id);
            userCache.add(USER_REMEMBER_CACHE + id);
        }
        redisTemplate.delete(userCache);
    }

}

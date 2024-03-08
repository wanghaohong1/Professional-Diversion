package com.glxy.pro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.glxy.pro.DTO.PageDTO;
import com.glxy.pro.DTO.VolunteerDTO;
import com.glxy.pro.bo.AdmissionBo;
import com.glxy.pro.bo.StudentBo;
import com.glxy.pro.bo.VolunteerBo;
import com.glxy.pro.entity.Volunteer;
import com.glxy.pro.mapper.VolunteerMapper;
import com.glxy.pro.query.StudentQuery;
import com.glxy.pro.query.VolunteerQuery;
import com.glxy.pro.service.IVolunteerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.glxy.pro.constant.RedisConstants.TWELVE_HOUR_TTL;
import static com.glxy.pro.constant.RedisConstants.VOLUNTEER_CACHE;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Service
public class VolunteerServiceImpl extends ServiceImpl<VolunteerMapper, Volunteer> implements IVolunteerService {
    @Autowired
    private VolunteerMapper volunteerMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<VolunteerBo> getVolunteerById(String stuId) {
        // 查缓存
        List<VolunteerBo> res = redisTemplate.opsForList().range(VOLUNTEER_CACHE + stuId, 0, -1);
        if (res != null && !res.isEmpty()) {
            // 缓存命中 直接返回
            return res;
        }else{
            // 缓存未命中 查数据库
            res = volunteerMapper.getVolunteerById(stuId);
            if (!res.isEmpty()) {
                // 查到了 构建缓存
                redisTemplate.opsForList().rightPushAll(VOLUNTEER_CACHE + stuId, res);
                redisTemplate.expire(VOLUNTEER_CACHE + stuId, TWELVE_HOUR_TTL, TimeUnit.SECONDS);
                return res;
            }
        }
        // 查不到 返回空
        return null;
    }

    @Override
    public boolean resetVolunteers(String stuId) {
        LambdaQueryWrapper<Volunteer> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Volunteer::getStuId, stuId);
        // 先删缓存
        redisTemplate.delete(VOLUNTEER_CACHE + stuId);
        // 再删数据库
        return remove(wrapper);
    }

    @Override
    public void removeBatchByStuIds(List<String> ids) {
        volunteerMapper.removeBatchByStuIds(ids);
    }

    public List<StudentBo> getUnFillStuByCateId(String cateId, Integer grade) {
        return volunteerMapper.getUnFillStuByCateId(cateId, grade);
    }

    @Override
    public int getFillCountByCateId(String cateId) {
        return volunteerMapper.getFillCountByCateId(cateId);
    }

    @Override
    public PageDTO<VolunteerDTO> getVolunteerByPagesAndConditions(StudentQuery query) {
//        return volunteerMapper.getVolunteerByPagesAndConditions(query);
//        // 1.构建条件
//        Page<AdmissionBo> page = query.toMpPageDefaultSort("adm_year");
//        page.setSearchCount(true);
//        // 2.查询
//        Integer begin = (query.getPageNo() - 1) * query.getPageSize();
//        List<AdmissionBo> admissionBos = admissionMapper.queryAdmissionPage(query, begin);
//        // 2.1 查询总数
//        Integer total = admissionMapper.queryAdmissionCount(query);
//        page.setRecords(admissionBos);
//        page.setTotal(total);
//        // 总页数
//        int totalPage = total % query.getPageSize() == 0 ? total / query.getPageSize() : total / query.getPageSize() + 1;
//        page.setPages(totalPage);
//        return PageDTO.of(page, AdmissionBo.class);
        // 1.构建条件
        Page<VolunteerDTO> page = query.toMpPageDefaultSort("stu_id");
        page.setSearchCount(true);
        // 2.查询
        query.setSortBy("which");
        Integer begin = (query.getPageNo() - 1) * query.getPageSize();
        List<VolunteerDTO> volunteerBos = volunteerMapper.getVolunteerByPagesAndConditions(query, begin);
        // 2.1 查询总数
        Integer total = volunteerMapper.queryVolunteerCount(query);
        page.setRecords(volunteerBos);
        page.setTotal(total);
        // 总页数
        int totalPage = total % query.getPageSize() == 0 ? total / query.getPageSize() : total / query.getPageSize() + 1;
        page.setPages(totalPage);
        return PageDTO.of(page, VolunteerDTO.class);
    }


    @Override
    public List<VolunteerBo> getByCategoryIdAndLib(String categoryId, int lib, Integer grade) {
        return volunteerMapper.getByCategoryIdAndLib(categoryId, lib, grade);
    }

}

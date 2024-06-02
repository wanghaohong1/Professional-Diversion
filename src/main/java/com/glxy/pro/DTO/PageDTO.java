package com.glxy.pro.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@ApiModel(description = "分页结果")
public class PageDto<T> {
    @ApiModelProperty("总条数")
    private Long total;
    @ApiModelProperty("总页数")
    private Long pages;
    @ApiModelProperty("集合")
    private List<T> list;

    public static <PO, BO> PageDto<BO> of(Page<PO> p, Class<BO> boClass) {
        PageDto<BO> dto = new PageDto<>();
        // 1. 总条数
        dto.setPages(p.getPages());
        // 2. 总页数
        dto.setTotal(p.getTotal());

        // 3.当前页面数据非空校验
        List<PO> records = p.getRecords();
        // 3.数据非空校验
        if (CollectionUtils.isEmpty(records)) {
            dto.setList(Collections.emptyList());
            return dto;
        }

        // 4.有数据，拷贝返回
        List<BO> boList = new ArrayList<>();
        for (PO po : records) {
            BO bo;
            try {
                bo = boClass.newInstance(); // 使用反射创建 BO 对象
                BeanUtils.copyProperties(po, bo);
                boList.add(bo);
            } catch (InstantiationException | IllegalAccessException e) {
                // 处理异常
                e.printStackTrace();
            }
        }
        dto.setList(boList);
        return dto;
    }
}

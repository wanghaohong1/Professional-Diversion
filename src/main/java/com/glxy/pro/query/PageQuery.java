package com.glxy.pro.query;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分页查询实体")
public class PageQuery {
    @ApiModelProperty("页码")
    private Integer pageNo = 1;
    @ApiModelProperty("每页数据条数")
    private Integer pageSize = 5;
    @ApiModelProperty("排序字段")
    private String sortBy;
    @ApiModelProperty("是否升序")
    private Boolean isAsc = true;

    public <T> Page<T> toMpPage(OrderItem ... items) {
        // 1.1 分页条件
        Page<T> page = Page.of(pageNo, pageSize, isAsc);
        // 1.2 排序条件
        if(StrUtil.isNotBlank(sortBy)) {
            page.addOrder(new OrderItem(sortBy, isAsc));
        }else if(items != null && items.length > 0){
            //默认排序
            page.addOrder(items);
        }
        return page;
    }

    public <T> Page<T> toMpPageDefaultSort(String defaultSortBy) {
        return toMpPage(new OrderItem(defaultSortBy, isAsc));
    }
}

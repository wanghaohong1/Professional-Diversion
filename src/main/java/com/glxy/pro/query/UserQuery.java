package com.glxy.pro.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author Alonha
 * @create 2023-10-16-0:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQuery extends PageQuery{
    /**
     * 用户ID列表
     */
    @ApiModelProperty("用户ID列表")
    private List<String> userIds;


}

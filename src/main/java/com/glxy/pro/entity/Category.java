package com.glxy.pro.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lgynb
 * @since 2024-01-25
 */
@Data
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 大类ID
     */
    @TableId("category_id")
    private String categoryId;

    /**
     * 大类名称
     */
    @TableField("category_name")
    private String categoryName;

    /**
     * 学生总数
     */
    @TableField("stu_num")
    private Integer stuNum;
}

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
public class Major implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专业ID
     */
    @TableId(value = "major_id")
    private String majorId;

    /**
     * 专业名称
     */
    @TableField(value = "major_name")
    private String majorName;

    /**
     * 所属大类ID
     */
    @TableField(value = "category_id")
    private String categoryId;
}

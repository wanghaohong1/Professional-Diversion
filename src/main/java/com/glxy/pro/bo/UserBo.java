package com.glxy.pro.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alonha
 * @create 2023-10-13-21:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBo {
    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 记住我（7天免登录）
     */
    private boolean remember;
}

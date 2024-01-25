package com.glxy.pro.bo;

import lombok.Data;


@Data
public class EmailBo {
    /**
     * 验证码
     */
    private String verification;

    /**
     * 联系人手机号
     */
    private String phone;
}

package com.glxy.pro.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Alonha
 * @create 2023-11-24-23:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAndStudentBo {
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
     * 学生姓名
     */
    private String name;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 年级
     */
    private String grade;

    /**
     * 所属大类名称
     */
    private String category;


    /**
     * 学生班级
     */
    private String stuClass;


}

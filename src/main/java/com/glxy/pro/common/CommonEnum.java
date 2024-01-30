package com.glxy.pro.common;

/**
 * 自定义错误枚举类
 */
public enum CommonEnum implements BaseErrorInfo {
    // 成功 0
    SUCCESS(200, "成功"),
    // 登录错误 1~50
    NEED_LOGIN(1, "需要登录后操作"),
    USERNAME_PASSWORD_ERROR(2, "账号密码错误"),
    EMAIL_IS_NOT_VALID(3, "邮箱号码不合法"),
    PHONE_IS_NOT_VALID(4, "手机号码不合法"),
    MAIL_SEND_ERROR(5, "邮箱不存在或网络异常"),
    PHONE_IS_BIND(6, "手机号已被绑定"),
    EMAIL_IS_BIND(7, "邮箱已被绑定"),
    // TOKEN 50~100
    TOKEN_INVALID(50, "无效的TOKEN"),
    TOKEN_EXPIRE(51, "TOKEN已过期"),
    TOKEN_REQUIRE(52, "TOKEN是必须的"),
    // 参数错误 500~1000
    PARAM_REQUIRE(500, "缺少参数"),
    PARAM_INVALID(501, "无效参数"),
    PARAM_FILE_FORMAT_ERROR(502, "文件格式有误"),
    SERVER_ERROR(503, "服务器内部错误"),
    USERNAME_PASSWORD_MATCH(504, "密码不能与用户名一致"),
    NET_ERROR(505, "网络错误"),
    FEIGN_TIMEOUT_ERROR(506, "Feign调用超时"),

    NET_FLUCTUATION_ERROR(506, "网络异常波动"),

    // 数据错误 1000~2000
    DATA_EXIST(1000, "数据已经存在"),
    USER_DATA_NOT_EXIST(1001, "用户数据不存在"),
    DATA_NOT_EXIST(1002, "数据不存在"),
    EXCEL_INVALID(1003, "Excel文件有误"),
    EXCEL_HAS_EMPTY_CELL(1010, "Excel文件存在空数据项"),
    EXCEL_EMPTY(1011, "Excel文件为空"),
    NO_INFO(1004, "没有获取到任何数据"),
    SAME_DATA(1005, "存在相同数据"),
    // 权限错误 3000~3500
    NOT_ADMISSION(1006, "有专业没有设置招生计划"),
    PHONE_NO_USER(1007, "该手机号尚未绑定用户"),
    EMAIL_NO_USER(1008, "该邮箱尚未绑定用户"),
    USER_NO_LOGIN(1009, "用户尚未登录"),
    USER_GET_OUT(1009, "当前用户已被挤下线"),
    CATEGORY_DATA_NOT_EXIST(1010, "大类专业数据不存在"),
    ADD_ADM_SUCCESS(200, "添加招生计划成功"),
    UPDATE_ADM_SUCCESS(200, "修改招生计划成功"),
    NO_OPERATOR_AUTH(3000, "无权限操作"),
    NEED_ADMIN(3001, "需要管理员权限");

    //数据操作错误定义
    /**
     * 错误码
     */
    private final Integer resultCode;

    /**
     * 错误描述
     */
    private final String resultMsg;

    CommonEnum(Integer resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    @Override
    public Integer getResultCode() {
        return this.resultCode;
    }

    @Override
    public String getResultMsg() {
        return this.resultMsg;
    }
}

package com.glxy.pro.common;

/**
 * 自定义描述错误接口
 */
public interface BaseErrorInfo {
    /**
     * 错误码
     * @return 错误码
     */
    Integer getResultCode();

    /**
     * 错误描述
     * @return 错误描述
     */
    String getResultMsg();
}

package com.glxy.pro.config;


import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import com.glxy.pro.common.CommonEnum;
import com.glxy.pro.common.ResultBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.SocketTimeoutException;

/**
 * 自定义全局异常处理器
 *
 * @author lgynb
 */
//@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理token过期或者没登录的异常
     */
    @ExceptionHandler(value = NotLoginException.class)
    @ResponseBody
    public ResultBody exceptionHandler(HttpServletRequest request, NotLoginException e) {
        logger.error("该用户没有登陆, 报错原因为：", e);

        if (StpUtil.isLogin()) {
            //清空session
            SaSession session = StpUtil.getSessionByLoginId(StpUtil.getLoginId());
            session.logout();
            //后台退出登录
            StpUtil.logout();
        }

        return ResultBody.error(CommonEnum.NEED_LOGIN);

    }

    @ExceptionHandler(value = SocketTimeoutException.class)
    @ResponseBody
    public ResultBody exceptionHandler(HttpServletRequest request, SocketTimeoutException e) {
        logger.error("网络异常波动:", e);
        return ResultBody.error(CommonEnum.NET_FLUCTUATION_ERROR);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultBody exceptionHandler(HttpServletRequest req, Exception e) {
        logger.error("未知异常！原因是:", e);

        return ResultBody.error(CommonEnum.SERVER_ERROR);

    }

}

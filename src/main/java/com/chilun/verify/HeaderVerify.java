package com.chilun.verify;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 齿轮
 * @date 2023-12-04-10:38
 */
@Aspect
@Component
public class HeaderVerify {
    @Value("${gateway_verify.enable}")
    private boolean enable;
    @Value("${gateway_verify.enable_ip_verify}")
    private boolean enableIP;
    @Value("${gateway_verify.enable_ip_header}")
    private boolean enableHeader;
    @Value("${gateway_verify.ip}")
    private String ip;
    @Value("${gateway_verify.headerKey}")
    private String headerKey;
    @Value("${gateway_verify.headerValue}")
    private String headerValue;

    @Before("execution(* com.chilun.controller.AnalysisController.*(..))")
    public void verifyFrom() throws Exception {
//        System.out.println("切入");
        //获得参数：请求
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        //进行验证：ip错误或指定header不存在
        if (enable) {//进入校验条件：启用校验
            if (enableIP && !request.getRemoteAddr().equals(ip)) {
                throw new Exception("Verify IP Failure!");
            }
            if (enableHeader && !request.getHeader(headerKey).equals(headerValue)) {
                throw new Exception("Verify Header Failure!");
            }
        }
    }
}

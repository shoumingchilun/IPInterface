package com.chilun.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 齿轮
 * @date 2023-12-04-9:19
 */
@RestController
@RequestMapping("/analysis")
public class AnalysisController {
    @GetMapping("/ip")
    public String analysisIP(HttpServletRequest request) {
        String ret = "";
        String remoteAddr = request.getRemoteAddr();
        String originAddr = request.getHeader("X-Forwarded-For");
        ret += originAddr == null ? remoteAddr : remoteAddr + "\n代理前地址" + originAddr;
        String referer = request.getHeader("Referer");
        ret += referer == null ? "" : "\n来源：" + referer;
        return ret;
    }

    @GetMapping("/head")
    public String analysisHeader(HttpServletRequest request) throws JsonProcessingException {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> map = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            map.put(headerName, headerValue);
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(map);
    }

    @GetMapping("/body")
    public String analysisBody(HttpServletRequest request) throws IOException {
        // 获取请求体的输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流中的数据
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        // 打印请求体内容
        return requestBody.toString();
    }

    @GetMapping("/line")
    public String analysisLine(HttpServletRequest request) {
        // 获取请求方法（GET、POST等）
        String method = request.getMethod();
        // 获取请求URI（统一资源标识符）
        String uri = request.getRequestURI();
        // 获取查询字符串（如果有）
        String queryString = request.getQueryString();
        return "method=" + method + "\n" +
                "URI=" + uri + "\n" +
                (queryString == null ? "" : "queryString=" + queryString);
    }
}

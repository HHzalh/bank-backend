package com.king.bankbackend.interceptor;

import com.king.bankbackend.constant.JwtClaimsConstant;
import com.king.bankbackend.context.BaseContext;
import com.king.bankbackend.properties.JwtProperties;
import com.king.bankbackend.utils.JwtUtil;
import com.king.bankbackend.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader(jwtProperties.getAdminTokenName());
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long adminId = Long.valueOf(claims.get(JwtClaimsConstant.Admin_ID).toString());

            // 从Redis中获取token并进行比对
            String redisToken = redisUtil.getAdminToken(adminId);
            if (redisToken == null || !redisToken.equals(token)) {
                log.error("管理员token无效或已过期");
                response.setStatus(401);
                return false;
            }

            BaseContext.setCurrentId(adminId);
            return true;
        } catch (Exception ex) {
            log.error("JWT解析异常: {}", ex.getMessage());
            response.setStatus(401);
            return false;
        }
    }
}
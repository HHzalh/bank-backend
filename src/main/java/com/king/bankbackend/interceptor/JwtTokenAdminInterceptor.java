package com.king.bankbackend.interceptor;


import com.king.bankbackend.constant.JwtClaimsConstant;
import com.king.bankbackend.context.BaseContext;
import com.king.bankbackend.properties.JwtProperties;
import com.king.bankbackend.utils.JwtUtil;
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
        log.info("获取到的令牌: {}", token);

        try {
            log.info("jwt校验: {}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long adminId = Long.valueOf(claims.get(JwtClaimsConstant.Admin_ID).toString());
            log.info("当前管理员id: {}", adminId);
            BaseContext.setCurrentId(adminId);
            return true;
        } catch (Exception ex) {
            log.error("JWT验证失败: {}", ex.getMessage());
            response.setStatus(401);
            return false;
        }
    }
}

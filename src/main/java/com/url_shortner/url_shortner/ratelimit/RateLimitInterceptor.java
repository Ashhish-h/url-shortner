package com.url_shortner.url_shortner.ratelimit;

import com.url_shortner.url_shortner.cache.RedisCacheService;
import com.url_shortner.url_shortner.cache.impl.RedisCahceServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RedisCacheService redisCacheService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddr = request.getRemoteAddr();
        Boolean isAllowed =  redisCacheService.checkAndConsumeToken(ipAddr);
        if(!isAllowed){
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Too many requests, please try again later\"}");
            return false;
        }
        return true;
    }

}

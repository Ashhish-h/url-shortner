package com.url_shortner.url_shortner.cache.impl;

import com.url_shortner.url_shortner.cache.RedisCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisCahceServiceImpl implements RedisCacheService {

    @Value("${redis.default.ttl.seconds}")
    private Integer redisDefaultTTlSeconds;

    @Value("${redis.limit.max.tokens}")
    private Integer redisLimitMaxTokens;

    @Value("${redis.limit.ttl.seconds}")
    private Integer redisRateLimitTtl;

    private final StringRedisTemplate redisTemplate;
    private static final String URL_KEY = "URL_KEY:";
    private static final String CLICKS_KEY = "CLICKS_KEY:";
    private static final String ANALYTICS_KEY = "ANALYTICS_KEY:top";
    private static final String RATE_LIMIT_KEY = "RATE_LIMIT_KEY:";
    private static final Double CLICK_INCREASE_CONSTANT = 1.0;

    @Override
    public void cacheUrl(String shortCode, String originalUrl) {
        if(StringUtils.hasText(originalUrl)){
            try{
                redisTemplate.opsForValue().set(URL_KEY + shortCode, originalUrl, redisDefaultTTlSeconds, TimeUnit.SECONDS);
            } catch(Exception e){
                log.error("Failed to save value for key : {} with value : {} in redis with exception : {}", shortCode, originalUrl, e.getMessage());
            }
        }
    }

    @Override
    public void cacheUrl(String shortCode, String originalUrl, Long ttl) {
        if(StringUtils.hasText(originalUrl)){
            try{
                redisTemplate.opsForValue().set(URL_KEY + shortCode, originalUrl, ttl, TimeUnit.SECONDS);
            } catch(Exception e){
                log.error("Failed to save value for  key : {} with value : {} in redis with exception : {}", shortCode, originalUrl, e.getMessage());
            }
        }
    }

    @Override
    public String getCacheUrl(String shortCode) {
        if(StringUtils.hasText(shortCode)){
          try{
              return redisTemplate.opsForValue().get(URL_KEY + shortCode);
          } catch(Exception e){
              log.error("Failed to get value for key : {} with exception : {}", shortCode, e.getMessage());
          }
        }
        return null;
    }

    @Override
    public void invalidateUrl(String shortCode) {
        if(StringUtils.hasText(shortCode)){
            try{
                redisTemplate.delete(URL_KEY + shortCode);
            } catch(Exception e){
                log.error("Failed to delete value for key : {} with exception : {}", shortCode, e.getMessage());
            }
        }
    }

    @Override
    public void incrementClickCount(String shortCode) {
        if(StringUtils.hasText(shortCode)){
            try{
                redisTemplate.opsForValue().increment(CLICKS_KEY + shortCode);
            } catch(Exception e){
                log.error("Failed to increment click count for key : {} with exception : {}", shortCode, e.getMessage());
            }
        }
    }

    @Override
    public Long getClickCount(String shortCode) {
        if(StringUtils.hasText(shortCode)){
            try{
                String value = redisTemplate.opsForValue().get(CLICKS_KEY + shortCode);
                return value == null ? 0L : Long.parseLong(value);
            } catch (Exception e){
                log.error("Failed to get click count for key : {} with exception : {}", shortCode, e.getMessage());
            }
        }
        return 0L;
    }

    @Override
    public void updateTopUrls(String shortCode) {
        if(StringUtils.hasText(shortCode)){
            try{
                redisTemplate.opsForZSet().incrementScore(ANALYTICS_KEY, shortCode, CLICK_INCREASE_CONSTANT);
            } catch(Exception e){
                log.error("Failed to update top urls for key : {} with exception : {}", shortCode, e.getMessage());
            }
        }
    }

    @Override
    public Long getReverseRank(String shortCode) {
        if(StringUtils.hasText(shortCode)){
            try{
                Long rank = redisTemplate.opsForZSet().reverseRank(ANALYTICS_KEY, shortCode);
                return rank == null ? -1L : rank + 1; // since redis is zero based and -1 represent now rank.
            } catch (Exception e){
                log.error("Failed to get reverse rank for key : {} with exception : {}", shortCode, e.getMessage());
            }
        }
        return -1L;
    }

    @Override
    public Boolean checkAndConsumeToken(String ip) {
        try{
            String key = redisTemplate.opsForValue().get(RATE_LIMIT_KEY + ip);

            if(!StringUtils.hasText(key)){
                redisTemplate.opsForValue().set(RATE_LIMIT_KEY + ip, String.valueOf(redisLimitMaxTokens - 1), redisRateLimitTtl, TimeUnit.SECONDS);
                return true;
                }

            int limit = Integer.parseInt(key);

            if(limit == 0){
                return false;
            }

            if(limit > 0){
                redisTemplate.opsForValue().decrement(RATE_LIMIT_KEY + ip);
                return true;
            }

        } catch(Exception e){
            log.error("Exception while processing rate limit request : {}", e.getMessage());
        }
        return true;
    }
}

package com.url_shortner.url_shortner.cache;

public interface RedisCacheService {

    void cacheUrl(String shortCode, String originalUrl);

    String getCacheUrl(String shortCode);

}

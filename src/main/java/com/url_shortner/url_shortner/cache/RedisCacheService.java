package com.url_shortner.url_shortner.cache;

public interface RedisCacheService {

    void cacheUrl(String shortCode, String originalUrl);

    void cacheUrl(String shortCode, String originalUrl, Long ttl);

    String getCacheUrl(String shortCode);

    void invalidateUrl(String shortCode);

    void incrementClickCount(String shortCode);

    Integer getClickCount(String shortCode);

    void updateTopUrls(String shortCode);

    Boolean checkAndConsumeToken(String ip);

}

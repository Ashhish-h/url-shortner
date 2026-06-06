package com.url_shortner.url_shortner.service;

import com.url_shortner.url_shortner.dto.UrlRequest;
import com.url_shortner.url_shortner.dto.UrlResponse;

public interface UrlService {

    UrlResponse createShortUrl(UrlRequest urlRequest);

    String getOriginalUrl(String shortCode, String ip, String userAgent, String referrer);

    void deactivateUrl(String shortCode);

}

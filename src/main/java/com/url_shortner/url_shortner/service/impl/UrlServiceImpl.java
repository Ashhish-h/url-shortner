package com.url_shortner.url_shortner.service.impl;

import com.url_shortner.url_shortner.cache.RedisCacheService;
import com.url_shortner.url_shortner.dto.UrlRequest;
import com.url_shortner.url_shortner.dto.UrlResponse;
import com.url_shortner.url_shortner.encoder.Base62Encoder;
import com.url_shortner.url_shortner.entity.URL;
import com.url_shortner.url_shortner.event.ClickEvent;
import com.url_shortner.url_shortner.exception.UrlNotAccessibleException;
import com.url_shortner.url_shortner.exception.UrlNotFoundException;
import com.url_shortner.url_shortner.repository.UrlRepository;
import com.url_shortner.url_shortner.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.default.url-expiry-time}")
    private Integer defaultExpiryTime;

    private final UrlRepository urlRepository;
    private final Base62Encoder base62Encoder;
    private final RedisCacheService redisCacheService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public UrlResponse createShortUrl(UrlRequest urlRequest) {
        URL url = URL.builder()
                .originalUrl(urlRequest.getOriginalUrl())
                .shortCode(urlRequest.getOriginalUrl())
                .expiresAt(urlRequest.getExpiresAt() != null ? urlRequest.getExpiresAt() : LocalDateTime.now().plusDays(defaultExpiryTime))
                .isActive(Boolean.TRUE)
                .build();

        URL savedUrl = urlRepository.save(url);

        String shortUrl = base62Encoder.encode(savedUrl.getId());

        savedUrl.setShortCode(shortUrl);
        urlRepository.save(savedUrl);

        return UrlResponse.builder()
                .originalUrl(savedUrl.getOriginalUrl())
                .shortUrl(baseUrl + shortUrl)
                .shortCode(shortUrl)
                .createdAt(savedUrl.getCreatedTime())
                .expiredAt(savedUrl.getExpiresAt())
                .isActive(savedUrl.getIsActive())
                .build();
    }

    @Override
    public String getOriginalUrl(String shortCode, String ip, String userAgent, String referrer) {
        String originalUrl = redisCacheService.getCacheUrl(shortCode);
        if(originalUrl == null) {

            URL url = urlRepository.findByShortCode(shortCode)
                    .orElseThrow(() -> new UrlNotFoundException("Url not found with shortCode " +  shortCode));

            if(url.isAccessible()) {
                redisCacheService.cacheUrl(shortCode, url.getOriginalUrl());
                applicationEventPublisher.publishEvent(new ClickEvent(shortCode, ip, userAgent, referrer));
                return url.getOriginalUrl();
            } else {
                throw new UrlNotAccessibleException("Url is expired or inactive for shortCode " +  shortCode);
            }

        }

        applicationEventPublisher.publishEvent(new ClickEvent(shortCode, ip, userAgent, referrer));
        return originalUrl;
    }

    @Override
    @Transactional
    public void deactivateUrl(String shortCode) {
        URL url = urlRepository.findByShortCode(shortCode)
                        .orElseThrow(() -> new UrlNotFoundException("Url not found with shortCode " +  shortCode));
        url.setIsActive(Boolean.FALSE);
        urlRepository.save(url);
        redisCacheService.invalidateUrl(shortCode);
    }
}

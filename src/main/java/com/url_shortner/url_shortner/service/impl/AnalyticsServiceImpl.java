package com.url_shortner.url_shortner.service.impl;

import com.url_shortner.url_shortner.cache.RedisCacheService;
import com.url_shortner.url_shortner.dto.AnalyticsResponse;
import com.url_shortner.url_shortner.dto.ClickDetails;
import com.url_shortner.url_shortner.entity.UrlClick;
import com.url_shortner.url_shortner.event.ClickEvent;
import com.url_shortner.url_shortner.repository.UrlClickRepository;
import com.url_shortner.url_shortner.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UrlClickRepository urlClickRepository;
    private final RedisCacheService redisCacheService;

    @Async
    @Override
    @EventListener
    public void recordClick(ClickEvent clickEvent) {
        try{
            UrlClick urlClick = UrlClick.builder()
                    .shortCode(clickEvent.getShortCode())
                    .ipAddress(clickEvent.getIp())
                    .userAgent(clickEvent.getUserAgent())
                    .referrer(clickEvent.getReferrer())
                    .build();

            urlClickRepository.save(urlClick);

            redisCacheService.incrementClickCount(urlClick.getShortCode());
            redisCacheService.updateTopUrls(urlClick.getShortCode());
        } catch (Exception e){
            log.error("Failed to recoded click for shortCode: {} with exception: {}", clickEvent.getShortCode(), e.getMessage());
        }
    }

    @Override
    public AnalyticsResponse getAnalytics(String shortCode) {
        Long clickCount = redisCacheService.getClickCount(shortCode);
        if(clickCount == 0){
            clickCount = urlClickRepository.countByShortCode(shortCode);
        }

        Long urlRank = redisCacheService.getReverseRank(shortCode);
        List<UrlClick>  urlClicks = urlClickRepository.findTop10ByShortCode(shortCode);
        List<ClickDetails> clickDetails = urlClicks.stream()
                .map(urlClick-> ClickDetails.builder()
                        .clickedAt(urlClick.getClickedAt())
                        .ip(urlClick.getIpAddress())
                        .userAgent(urlClick.getUserAgent())
                        .referrer(urlClick.getReferrer())
                        .build())
                .toList();

        return AnalyticsResponse.builder()
                .url(shortCode)
                .totalClicks(clickCount)
                .rank(urlRank)
                .recentClicks(clickDetails)
                .build();

    }

    @Override
    public Long getRank(String shortCode) {
        return redisCacheService.getReverseRank(shortCode);
    }
}

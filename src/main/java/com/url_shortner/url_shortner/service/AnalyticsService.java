package com.url_shortner.url_shortner.service;

import com.url_shortner.url_shortner.dto.AnalyticsResponse;
import com.url_shortner.url_shortner.event.ClickEvent;
import org.springframework.stereotype.Service;

public interface AnalyticsService {

    void recordClick(ClickEvent clickEvent);

    AnalyticsResponse getAnalytics(String shortCode);

    Long getRank(String shortCode);
}

package com.url_shortner.url_shortner.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsResponse {
    private String url;
    private Long totalClicks;
    private Long rank;
    private List<ClickDetails> recentClicks;
}

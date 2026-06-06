package com.url_shortner.url_shortner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClickDetails {
    private LocalDateTime clickedAt;
    private String ip;
    private String userAgent;
    private String referrer;
}

package com.url_shortner.url_shortner.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClickEvent {
    private String shortCode;
    private String ip;
    private String userAgent;
    private String referrer;
}

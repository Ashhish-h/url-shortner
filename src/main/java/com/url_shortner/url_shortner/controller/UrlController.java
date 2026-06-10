package com.url_shortner.url_shortner.controller;

import com.url_shortner.url_shortner.dto.UrlRequest;
import com.url_shortner.url_shortner.dto.UrlResponse;
import com.url_shortner.url_shortner.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping(value = "/urls")
    public ResponseEntity<UrlResponse> createShortUrl(@Valid @RequestBody UrlRequest urlRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(urlService.createShortUrl(urlRequest));
    }

    @GetMapping(value = "/url/{shortcode}")
    public ResponseEntity<String> getOriginalUrl(@PathVariable String shortcode, HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(ipAddress == null) ipAddress = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");

        String originalUrl = urlService.getOriginalUrl(shortcode, ipAddress, userAgent, referer);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }

    @DeleteMapping(value = "/url/{shortcode}/deactivate")
    public ResponseEntity<UrlResponse> deactivateUrl(@PathVariable String shortcode) {
        urlService.deactivateUrl(shortcode);
        return ResponseEntity.ok().build();
    }


}

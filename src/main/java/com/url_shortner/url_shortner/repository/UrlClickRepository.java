package com.url_shortner.url_shortner.repository;

import com.url_shortner.url_shortner.entity.UrlClick;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UrlClickRepository extends JpaRepository<UrlClick, Long> {

    Long coundByShortCode(String shorCode);

    List<UrlClick> findTop10ByShortCode(String shorCode);
}

package com.url_shortner.url_shortner.repository;

import com.url_shortner.url_shortner.entity.UrlClick;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlClickRepository extends JpaRepository<UrlClick, Long> {
}

package com.url_shortner.url_shortner.repository;

import com.url_shortner.url_shortner.entity.URL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<URL, Long> {

    Optional<URL> findByShortCode(String shortCode);

}

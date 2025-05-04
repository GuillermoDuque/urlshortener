package com.dio.urlshortener.config;

import com.dio.urlshortener.application.ShortUrlService;
import com.dio.urlshortener.config.properties.AppProperties;
import com.dio.urlshortener.domain.repository.ShortUrlRepository;
import com.dio.urlshortener.domain.service.ShortCodeGenerator;
import com.dio.urlshortener.infrastructure.JpaShortUrlRepository;
import com.dio.urlshortener.infrastructure.SpringDataShortUrlRepository;
import com.dio.urlshortener.infrastructure.cache.ShortUrlCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShortUrlConfig {

    @Bean
    public ShortUrlService shortUrlService(ShortUrlRepository shortUrlRepository, ShortUrlCache shortUrlCache, AppProperties appProperties, ShortCodeGenerator generator) {
        return new ShortUrlService(shortUrlRepository, shortUrlCache, appProperties, generator);
    }

    @Bean
    public JpaShortUrlRepository jpaShortUrlRepository(SpringDataShortUrlRepository springDataShortUrlRepository) {
        return new JpaShortUrlRepository(springDataShortUrlRepository);
    }

}

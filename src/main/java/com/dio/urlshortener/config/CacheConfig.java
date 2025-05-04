package com.dio.urlshortener.config;

import com.dio.urlshortener.config.properties.CacheProperties;
import com.dio.urlshortener.infrastructure.cache.ShortUrlCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public ShortUrlCache shortUrlCache(CacheProperties properties){
        return new ShortUrlCache(properties);
    }
}

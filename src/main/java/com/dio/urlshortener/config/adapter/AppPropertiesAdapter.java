package com.dio.urlshortener.config.adapter;

import com.dio.urlshortener.application.port.BaseUrlProvider;
import com.dio.urlshortener.config.properties.AppProperties;
import org.springframework.stereotype.Component;

@Component
public class AppPropertiesAdapter implements BaseUrlProvider {
    private final AppProperties properties;

    public AppPropertiesAdapter(AppProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getBaseUrl(){
        return properties.getBaseUrl();
    }

}

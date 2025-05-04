package com.dio.urlshortener.domain.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "short_url", indexes = {
        @Index(name = "idx_shorturl_is_active", columnList = "isActive"),
        @Index(name = "idx_shorturl_long_url", columnList = "longUrl")
})
public class ShortUrl {

    @Id
    private String shortCode;
    @Column(nullable = false)
    private String longUrl;
    private boolean isActive;
    private Instant createdAt;
    private long accessCount;

    public ShortUrl(String shortCode, String longUrl){
        this.shortCode = shortCode;
        this.longUrl = longUrl;
        this.createdAt = Instant.now();
        this.isActive = true;
        this.accessCount = 0;
    }

    public ShortUrl() {
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }
}

package com.dio.urlshortener.infrastructure.cache;

import com.dio.urlshortener.common.BaseTestSupport;
import com.dio.urlshortener.config.properties.CacheProperties;
import com.dio.urlshortener.domain.model.ShortUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import static org.assertj.core.api.Assertions.assertThat;

class ShortUrlCacheTest extends BaseTestSupport {

    private ShortUrlCache cache;

    @BeforeEach
    void setUp() {
        CacheProperties props = new CacheProperties();
        props.setMaximumSize(100);
        props.setInitialCapacity(10);
        props.setExpireAfterWrite(Duration.ofMinutes(10));
        props.setRecordStats(true);

        cache = new ShortUrlCache(props);
        cache.init();
    }

    @Test
    void putAndGetIfPresent_shouldStoreAndRetrieveValue() {
        ShortUrl url = createUrl(SHORT_CODE, LONG_URL);

        cache.put(url);

        assertThat(cache.getIfPresent(SHORT_CODE))
                .isPresent()
                .contains(url);
    }

    @Test
    void getIfPresent_shouldReturnEmptyIfKeyMissing() {
        assertThat(cache.getIfPresent("missing")).isEmpty();
    }

    @Test
    void invalidate_shouldRemoveEntry() {
        ShortUrl url = createUrl(SHORT_CODE, LONG_URL);
        cache.put(url);

        cache.invalidate(SHORT_CODE);

        assertThat(cache.getIfPresent(SHORT_CODE)).isEmpty();
    }

    @Test
    void clear_shouldRemoveAllEntries() {
        cache.put(new ShortUrl("a", "https://a.com"));
        cache.put(new ShortUrl("b", "https://b.com"));

        cache.clear();

        assertThat(cache.getIfPresent("a")).isEmpty();
        assertThat(cache.getIfPresent("b")).isEmpty();
    }
}

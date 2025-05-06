package com.dio.urlshortener.application;

import com.dio.urlshortener.common.BaseTestSupport;
import com.dio.urlshortener.config.properties.AppProperties;
import com.dio.urlshortener.domain.model.ShortUrl;
import com.dio.urlshortener.domain.repository.ShortUrlRepository;
import com.dio.urlshortener.infrastructure.cache.ShortUrlCache;
import com.dio.urlshortener.infrastructure.generator.Base62ShortCodeGenerator;
import com.dio.urlshortener.presentation.dto.ShortenUrlRequest;
import com.dio.urlshortener.presentation.dto.ShortenUrlUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShortUrlServiceTest extends BaseTestSupport {

    private ShortUrlRepository repository;
    private ShortUrlCache cache;
    private ShortUrlService service;

    @BeforeEach
    void setUp() {
        repository = mock(ShortUrlRepository.class);
        cache = mock(ShortUrlCache.class);
        AppProperties properties = mock(AppProperties.class);
        service = new ShortUrlService(repository, cache, properties, new Base62ShortCodeGenerator());
    }

    @Test
    void createShortUrl_shouldSaveUrlAndPutInCache() {
        ShortenUrlRequest request = new ShortenUrlRequest(LONG_URL);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ShortUrl result = service.createShortUrl(request);

        ArgumentCaptor<ShortUrl> captor = ArgumentCaptor.forClass(ShortUrl.class);
        verify(repository).save(captor.capture());
        verify(cache).put(result);

        ShortUrl saved = captor.getValue();
        assertThat(saved.getShortCode()).isNotNull();
        assertThat(saved.getLongUrl()).isEqualTo(LONG_URL);
        assertThat(saved.isActive()).isTrue();
        assertThat(saved.getAccessCount()).isZero();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    void updateShortUrl_shouldUpdateValues() {
        ShortUrl existing = createUrl(SHORT_CODE, LONG_URL);
        when(repository.findByShortCode(SHORT_CODE)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ShortenUrlUpdateRequest request = new ShortenUrlUpdateRequest(NEW_URL, false);
        Optional<ShortUrl> result = service.updateShortUrl(SHORT_CODE, request);

        assertThat(result).isPresent();
        ShortUrl updated = result.get();
        assertThat(updated.getLongUrl()).isEqualTo(NEW_URL);
        assertThat(updated.isActive()).isFalse();
        verify(repository).save(updated);
        verify(cache, times(2)).put(any());
    }

    @Test
    void updateShortUrl_shouldReturnEmptyIfNotFound() {
        when(repository.findByShortCode("not-found")).thenReturn(Optional.empty());
        ShortenUrlUpdateRequest request = new ShortenUrlUpdateRequest(NEW_URL, true);

        Optional<ShortUrl> result = service.updateShortUrl("not-found", request);

        assertThat(result).isEmpty();
        verify(repository, never()).save(any());
        verify(cache, never()).put(any());
    }

    @Test
    void resolveShortUrl_shouldReturnFromCacheIfPresent() {
        ShortUrl cached = createUrl(SHORT_CODE, LONG_URL);
        when(cache.getIfPresent(SHORT_CODE)).thenReturn(Optional.of(cached));

        Optional<ShortUrl> result = service.resolveShortUrl(SHORT_CODE);

        assertThat(result).contains(cached);
        verify(repository, never()).findByShortCode(any());
    }

    @Test
    void findByShortCode_shouldFetchFromRepositoryAndCacheIt_ifNotInCache() {
        ShortUrl fromDb = createUrl("xyz789", "https://from-db.com");
        when(cache.getIfPresent("xyz789")).thenReturn(Optional.empty());
        when(repository.findByShortCode("xyz789")).thenReturn(Optional.of(fromDb));

        Optional<ShortUrl> result = service.findByShortCode("xyz789");

        assertThat(result).contains(fromDb);
        verify(cache).getIfPresent("xyz789");
        verify(repository).findByShortCode("xyz789");
        verify(cache).put(fromDb);
    }

    @Test
    void findByShortCode_shouldReturnFromCacheIfPresent() {
        ShortUrl cached = createUrl(SHORT_CODE, LONG_URL);
        when(cache.getIfPresent(SHORT_CODE)).thenReturn(Optional.of(cached));

        Optional<ShortUrl> result = service.findByShortCode(SHORT_CODE);

        assertThat(result).contains(cached);
        verify(cache).getIfPresent(SHORT_CODE);
        verify(repository, never()).findByShortCode(any());
    }

    @Test
    void resolveShortUrl_shouldReturnEmptyIfInactive() {
        ShortUrl inactive = createUrl(DISABLED_CODE, DISABLED_URL);
        inactive.setActive(false);

        when(cache.getIfPresent(DISABLED_CODE)).thenReturn(Optional.of(inactive));

        Optional<ShortUrl> result = service.resolveShortUrl(DISABLED_CODE);

        assertThat(result).isEmpty();
        verify(repository, never()).findByShortCode(any());
    }
}

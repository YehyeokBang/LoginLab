package com.example.loginlab.common.config;

import com.example.loginlab.common.CacheType;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Configuration
public class CaffeineCacheConfig {

    @Bean
    public List<CaffeineCache> caffeineConfig() {
        return Stream.of(CacheType.values())
                .map(cache -> new CaffeineCache(cache.getCacheName(), Caffeine.newBuilder()
                                .recordStats()
                                .expireAfterWrite(cache.getExpireAfterWrite(), TimeUnit.SECONDS)
                                .maximumSize(cache.getMaximumSize())
                                .build()
                        )
                )
                .toList();
    }

    @Bean
    public CacheManager cacheManager(List<CaffeineCache> caffeineCaches) {
        final SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caffeineCaches);

        return cacheManager;
    }

}

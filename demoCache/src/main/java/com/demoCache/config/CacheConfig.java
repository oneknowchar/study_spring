package com.demoCache.config;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class CacheConfig {
    public static final String PUBCOD = "PUBCOD";
    public static final String USERCACHE = "USERCACHE";

    public static Set<String> USERCACHE_KEYS = new ConcurrentSkipListSet<>();


    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
            new CaffeineCache(USERCACHE, caffeineCacheBuilder().build()),
            new CaffeineCache(PUBCOD, caffeineCacheBuilder().build())
        ));
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats();
    }
}

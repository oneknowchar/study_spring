package com.demoCache.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.demoCache.config.CacheConfig;
import com.demoCache.web.mapper.CacheMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {
    private final CacheMapper cacheMapper;
    private final CacheManager cacheManager;

    public Map<String, Object> getCode(String code) {
        Cache cache = cacheManager.getCache(CacheConfig.PUBCOD);
        Map<String, Object> cachedCode = cache != null ? cache.get(code, Map.class) : null;

        if(cachedCode == null) {
            log.debug("DB 조회 후 캐싱, 캐시값 리턴!");
            List<Map<String, Object>> codes = cacheMapper.getCodes();
            codes.forEach(codeData -> cache.put(codeData.get("CODVAL").toString(), codeData));
            cachedCode = cache.get(code, Map.class);
        }else {
            log.debug("캐싱된 값을 리턴!");
        }

        return cachedCode;	// 캐시에서 찾은 데이터 반환 (없으면 null)
    }
}

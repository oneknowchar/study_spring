package com.demoCache.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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


    // 이 방법은 키가 test로 고정, 단일 캐시만 가능 비추천, 직접 캐시매니저로 관리해야함
    @Cacheable(cacheNames = CacheConfig.USERCACHE, key = "#p0")
    public String getUsercache(String key) {
        setUsercache(key);

        log.debug("캐싱 되었습니다");
        String value = key + "_test123";

        return value;
    }

    public Object[] getCacheKeys(){
        CacheConfig.USERCACHE_KEYS.toArray();
        return CacheConfig.USERCACHE_KEYS.toArray();
    }

    public void setUsercache(String key) {
        Cache cache = cacheManager.getCache(CacheConfig.USERCACHE);
        cache.put(key, CacheConfig.USERCACHE_KEYS.size());	// 캐시 추가
        CacheConfig.USERCACHE_KEYS.add(key);				// 캐시 키 추가
    }

    public int delCache(String key) {
        Cache cache = cacheManager.getCache(CacheConfig.USERCACHE);

        if(CacheConfig.USERCACHE_KEYS.contains(key)) {
            cache.evict(key);						// 캐시 제거
            CacheConfig.USERCACHE_KEYS.remove(key);	// 캐시 키 제거
            log.debug("캐시 삭제 성공");
            return 1;
        }else {
            log.debug("캐시 삭제 실패");
            return 0;
        }
    }



}

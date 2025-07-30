package com.demoCache.web.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demoCache.config.CacheConfig;
import com.demoCache.web.service.CacheService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CacheController {
    private final CacheService cacheService;

    @GetMapping("/index")
    public String index(Model model, @RequestParam(required = false, value = "inputVal") String inputVal) {
        model.addAttribute("userCacheKeys", CacheConfig.USERCACHE_KEYS);

        // 개인 캐시 테스트
        if(inputVal == null || "".equals(inputVal)) {
            return "index";
        }else if(inputVal.startsWith("my")) {
            model.addAttribute("getUsercache", cacheService.getUsercache(inputVal));

        }else if(inputVal != null && !"".equals(inputVal)) {
            Map<String, Object> code = cacheService.getCode(inputVal);
            model.addAttribute("code", code);
            model.addAttribute("inputVal", inputVal);

        }else {
        }
        return "index";
    }

    @PostMapping("/delCache")
    @ResponseBody
    public int delCache(@RequestBody Map<String, String> data) {
        return cacheService.delCache(data.get("key"));
    }
}

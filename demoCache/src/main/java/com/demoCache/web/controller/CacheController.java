package com.demoCache.web.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demoCache.web.service.CacheService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CacheController {
    private final CacheService cacheService;

    @GetMapping("/index")
    public String index(Model model, @RequestParam(required = false, value = "inputVal") String inputVal) {
        if(inputVal != null && !"".equals(inputVal)) {
            Map<String, Object> code = cacheService.getCode(inputVal);
            model.addAttribute("code", code);
            model.addAttribute("inputVal", inputVal);
        }
        return "index";
    }
}

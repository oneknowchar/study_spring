package com.demoWebsocket.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.demoWebsocket.web.service.IndexService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
	private final IndexService indexService;
	
	@GetMapping("/")
	public String index(Model model) {
		model.addAttribute("message", "world!321321");
		model.addAttribute("time", indexService.getTime());
		return "index";
	}
}

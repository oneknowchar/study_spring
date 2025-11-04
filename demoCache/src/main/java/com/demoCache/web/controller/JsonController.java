package com.demoCache.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class JsonController {

	@GetMapping("/jsonIndex")
	public String index(Model model) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		Path path = Paths.get("C:/Users/pmg/Desktop/다건 선택/json/sido.json");
		
		if(Files.exists(path)) {
			try(InputStream ips = Files.newInputStream(path)){
				Map<String, Object> jsonMap= objectMapper.readValue(ips, Map.class);
				ArrayList objectList = (ArrayList<Map<String, Object>>)jsonMap.get("features");
				Map<String, Object> object = (Map<String, Object>) objectList.get(0);
				model.addAttribute("jsonMap", jsonMap);
			}
		}
		
		return "jsonIndex";
	}
}

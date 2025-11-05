package com.demoCache.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class JsonController {

    private final CacheController cacheController;

	List<Map<String, Object>> sidoProp = new ArrayList<>();
	List<Map<String, Object>> sigProp = new ArrayList<>();
	List<Map<String, Object>> emdProp = new ArrayList<>();


    JsonController(CacheController cacheController) {
        this.cacheController = cacheController;
    }
	
	
	@GetMapping("/jsonIndex")
	@ResponseBody
	public List<Map<String, Object>> index(@RequestParam(name = "jsonFileName", required = false) String jsonFileName) throws IOException {
		if(jsonFileName == null) {
			jsonFileName = "sido.json";
		}else {
			jsonFileName += ".json";	//sido, sig, emd
		}
		
		String dir = "X:/PROJECT/[2025.09] 티키타카/지도/HTML_소스코드/다건 선택_커스텀/json/";
		
		List<Map<String, Object>> props = new ArrayList<>();
		
		extractedData(dir, jsonFileName, props);
		
		return props;
		
	}
	
	@GetMapping("/jsonIndexHtml")
	public String jsonIndexHtml(Model model, @RequestParam(name = "jsonFileName", required = false) String jsonFileName) throws IOException {
		String dir = "X:/PROJECT/[2025.09] 티키타카/지도/HTML_소스코드/다건 선택_커스텀/json/";
		
		//List<Map<String, Object>> sidoProp = new ArrayList<>();
		//List<Map<String, Object>> sigProp = new ArrayList<>();
		//List<Map<String, Object>> emdProp = new ArrayList<>();
		
		// 데이터 역직렬화
		extractedData(dir, "sido.json", sidoProp);
		extractedData(dir, "sig.json", sigProp);
		extractedData(dir, "emd.json", emdProp);
		
		// 렌더 데이터
		model.addAttribute("sidoProp", sidoProp);
		model.addAttribute("sigProp", sigProp);
		model.addAttribute("emdProp", emdProp);
		
		return "jsonIndex";
	}
	@GetMapping("/jsonIndexAjax")
	public String jsonIndexAjax() {
		return "jsonIndexAjax";
	}
	
	@PostMapping("/jsonIndexJson")
	@ResponseBody
	public Map<String, Object> jsonIndexJson() throws IOException {
		String dir = "X:/PROJECT/[2025.09] 티키타카/지도/HTML_소스코드/다건 선택_커스텀/json/";
		
		//List<Map<String, Object>> sidoProp = new ArrayList<>();
		//List<Map<String, Object>> sigProp = new ArrayList<>();
		//List<Map<String, Object>> emdProp = new ArrayList<>();
		
		// 데이터 역직렬화
		extractedData(dir, "sido.json", sidoProp);
		extractedData(dir, "sig.json", sigProp);
		extractedData(dir, "emd.json", emdProp);
		
		// 렌더 데이터
		Map<String, Object> region = new HashMap<>();
		region.put("sido", sidoProp);
		region.put("sig", sigProp);
		region.put("emd", emdProp);
		return region;
	}

	private void extractedData(String dir, String jsonFileName, List<Map<String, Object>> propsList)
			throws IOException, StreamReadException, DatabindException {
		ObjectMapper objectMapper = new ObjectMapper();
		
		if(propsList.size() > 0 ) return;
		
		
		Path path = Paths.get(dir, jsonFileName);
		
		// 파일 여부 체크
		if(Files.exists(path)) {
			try(InputStream ips = Files.newInputStream(path)){
				Map<String, Object> jsonMap= objectMapper.readValue(ips, Map.class);
				
				// 역직렬화 진행
				List<Map<String, Object>> units = (List<Map<String, Object>>)jsonMap.get("features");

				for (Map<String, Object> unit : units) {
					Map<String, Object> properties = (Map<String, Object>)unit.get("properties");
					
					// 시 도 : CTPRVN_CD=11, CTP_ENG_NM=Seoul, CTP_KOR_NM=서울특별시
					// 시군구 : SIG_CD=11110, SIG_ENG_NM=Jongno-gu, SIG_KOR_NM=종로구
					// 읍면동 : EMD_CD=11110101, EMD_ENG_NM=Cheongun-dong, EMD_KOR_NM=청운동
					propsList.add(properties);
				}
				
				System.out.println(propsList.size());
				
				if(jsonFileName.startsWith("sido")) {
					propsList.sort(Comparator.comparing(m -> (String) m.get("CTP_KOR_NM")));	// 지역명순 정렬
				} else if(jsonFileName.startsWith("sig")) {
					propsList.sort(Comparator.comparing(m -> (String) m.get("SIG_KOR_NM")));	// 지역명순 정렬
				} else if(jsonFileName.startsWith("emd")) {
					propsList.sort(Comparator.comparing(m -> (String) m.get("EMD_KOR_NM")));	// 지역명순 정렬
				} else {
					throw new IllegalArgumentException("features는 List가 아닙니다.");
				}
				
			}
		}
	}
}

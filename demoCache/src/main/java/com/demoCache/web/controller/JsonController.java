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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class JsonController {
	// 임시 캐싱
	Map<String, Object> regionCodes = new HashMap<>();	// 행정구역 코드 selectBox 랜더
	
	@GetMapping("/addrCodeData")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addrCodeData() throws IOException {
		// 데이터 역직렬화
		extractedData("sido");
		extractedData("sig");
		extractedData("emd");
		
		// 행정구역코드
		return new ResponseEntity<Map<String,Object>>(regionCodes, HttpStatus.OK);
	}

	private void extractedData(String regionLevel)
			throws IOException, StreamReadException, DatabindException {
		ObjectMapper objectMapper = new ObjectMapper();
		String dir = "X:/PROJECT/[2025.09] 티키타카/지도/HTML_소스코드/다건 선택_커스텀/json/";
		
		if(regionCodes.get(regionLevel) != null) return;
		
		List<Map<String, Object>> propsList = new ArrayList<>();

		Path path = Paths.get(dir, regionLevel + ".json");
		
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
				
				if(regionLevel.equals("sido")) {
					propsList.sort(Comparator.comparing(m -> (String) m.get("CTP_KOR_NM")));	// 지역명순 정렬
				} else if(regionLevel.equals("sig")) {
					propsList.sort(Comparator.comparing(m -> (String) m.get("SIG_KOR_NM")));	// 지역명순 정렬
				} else if(regionLevel.equals("emd")) {
					propsList.sort(Comparator.comparing(m -> (String) m.get("EMD_KOR_NM")));	// 지역명순 정렬
				} else {
					throw new IllegalArgumentException("features는 List가 아닙니다.");
				}
				
				regionCodes.put(regionLevel, propsList);
			}
		}
	}
}

package com.demoWebsocket.web.service;

import org.springframework.stereotype.Service;

import com.demoWebsocket.web.mapper.IndexMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndexService {
	private final IndexMapper indexMapper;
	
	public String getTime() {
		return indexMapper.getTime();
	}
}

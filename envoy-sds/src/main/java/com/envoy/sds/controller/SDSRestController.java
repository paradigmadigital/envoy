package com.envoy.sds.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.envoy.sds.service.EndpointsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SDSRestController {

	@Autowired
	private EndpointsService endpointService;
	
	@GetMapping("/v1/registration/{serviceName}")
	public Map<String, Object> getServiceInstances(@PathVariable String serviceName) {
		
		log.info("GET -> /v1/registration/{serviceName={}}", serviceName);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			result.put("hosts", endpointService.getServiceEndpoint(serviceName));
		} catch (Exception e) {
			result = null;
		}
		
		return result;
	}
}
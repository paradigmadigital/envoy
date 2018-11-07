package com.envoy.sds.repository;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.envoy.sds.model.Endpoint;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class KubernetesRepositoryImpl implements KubernetesRepository {

	private static final String SERVICE_ENDPOINT = "/api/v1/namespaces/{namespace}/endpoints/{serviceName}";
	private static final String BEARER = "Bearer ";
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${kubernetes.namespace}")
	private String namespace;
	
	@Value("${kubernetes.api.host}")
	private String hostApi;
	
	@Value("${kubernetes.token.path}")
	private String tokenPath;
	
	public Endpoint getServiceEndpoint(String serviceName) throws IOException {
	
		HttpHeaders headers = new HttpHeaders();
		headers.add(AUTHORIZATION, BEARER + getToken());
		
		URI uri = fromUriString(hostApi + SERVICE_ENDPOINT).buildAndExpand(namespace, serviceName).toUri();
		
		RequestEntity<Endpoint> request = new RequestEntity<Endpoint>(headers, GET, uri);
		ResponseEntity<Endpoint> response = restTemplate.exchange(request, Endpoint.class);
		
		switch (response.getStatusCodeValue()) {
		case 401:
			log.error("Received 401 from Kubernetes API");
			throw new SecurityException("Received 401 from Kubernetes API");
		case 404:
			log.error("Provided serviceName seems to not exist: " + serviceName);
			throw new IllegalArgumentException("Provided serviceName seems to not exist: " + serviceName);
		}
		
		return response.getBody();
	}
	
	private String getToken() throws IOException {
		
		String result = null;
		FileInputStream inputStream = null;
		
		try {
			
			inputStream = new FileInputStream(new File(tokenPath));
			result = new String(inputStream.readAllBytes());
		} catch (IOException e) {
			log.error("Error trying to read token from: " + tokenPath);
			throw e;
		} finally {
			inputStream.close();
		}
		
		return result;
	}
}

package com.envoy.sds.repository;

import java.io.IOException;

import org.springframework.stereotype.Repository;

import com.envoy.sds.model.Endpoint;

@Repository
public interface KubernetesRepository {

	/**
	 * Retrieve a service information from the endpoints details
	 * 
	 * @param serviceName 
	 * @return
	 */
	Endpoint getServiceEndpoint(String serviceName) throws IOException;
}

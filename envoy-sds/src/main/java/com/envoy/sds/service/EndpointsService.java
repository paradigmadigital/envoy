package com.envoy.sds.service;

import java.io.IOException;
import java.util.List;

import com.envoy.sds.model.Host;

public interface EndpointsService {

	/**
	 * Retrieve the list of instances for a service
	 * 
	 * @param serviceName
	 * @return
	 */
	List<Host> getServiceEndpoint(String serviceName) throws IOException;
}

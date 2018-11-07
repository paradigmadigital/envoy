package com.envoy.sds.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.envoy.sds.model.Address;
import com.envoy.sds.model.Endpoint;
import com.envoy.sds.model.Host;
import com.envoy.sds.model.Tag;
import com.envoy.sds.repository.KubernetesRepository;

@Service
public class EndpointServiceImpl implements EndpointsService {

	@Autowired
	private KubernetesRepository kubernetesRepository;
	
	@Value("${envoy.port}")
	private Integer envoyPort;
	
	public List<Host> getServiceEndpoint(String serviceName) throws IOException {
		
		return map(kubernetesRepository.getServiceEndpoint(serviceName));
	}
	
	private List<Host> map(Endpoint endpoint) {
		
		List<Host> result = new ArrayList<Host>();
		
		/* Mandatory to add this because if empty Envoy is not able to handle 
		 * the response and does not add the instances */
		Tag tag = new Tag();
		tag.setAz("Spain");
		tag.setCanary(false);
		tag.setLoad_balancing_weight(1);
		
		for (Address address : endpoint.getSubsets().get(0).getAddresses()) {
			Host host = new Host();
			host.setIp_address(address.getIp());
			
			/* Port where Envoy instance listens for incoming connections, 
			 * not instance port */
			host.setPort(envoyPort);
			host.setTags(tag);
			result.add(host);
		}
		
		return result;
	}
}

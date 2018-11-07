package com.envoy.petstore.controller;

import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.envoy.petstore.model.Pet;
import com.envoy.petstore.model.PetStore;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class PetStoreController {
	
	private static final String PET_URI = "/pets?petStoreId={petStoreId}";
	
	@Autowired
	private RestTemplate restTemplate;
	
	private Integer status = HttpStatus.OK.value();
	
	@GetMapping(path = "/petstore/{id}")
	public PetStore getPetStore(@PathVariable String id, @RequestParam(required = true) boolean includePets) {
		
		log.info("GET /petstore/{id={}}?includePets={}", id, includePets);
		
		PetStore petStore = new PetStore("Amiguitos Mascotas", "Málaga", null);
		List<Pet> pets = includePets ? getPets(id) : new ArrayList<Pet>();
		petStore.setPets(pets);
		
		return petStore;
	}
	
	private List<Pet> getPets(String petStoreId) {
		
		List<Pet> result = new ArrayList<Pet>();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HOST, "pet");
		
		/* You need to add this because if not Host header is rewritten to match the host in the URL
		 *  and envoy doesn't recognize that host. This way the header maintains the value we included */
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		
		URI uri = fromUriString("http://localhost:9900" + PET_URI).buildAndExpand(petStoreId).toUri();
		RequestEntity<String> request = new RequestEntity<String>(headers, HttpMethod.GET, uri);
		
		log.info("Calling to Pet Service");
		ResponseEntity<List<Pet>> response = (ResponseEntity<List<Pet>>) 
				restTemplate.exchange(request, result.getClass());
		log.info("Response from Pet Service: {}", response);
		
		if (!response.getStatusCode().is2xxSuccessful()) {
			log.error("Error calling pet service: {}", response.getStatusCode());
		} else {
			result = response.getBody();
		}
		
		return result;
	}
	
	@GetMapping(path = "/health") 
	public void getHealth(HttpServletRequest request, HttpServletResponse response){
		log.info("GET /health");
		response.setStatus(status);
	}
	
	@PostMapping(path = "/health/{status}") 
	public void setHealth(@PathVariable Integer status){
		log.info("POST /health/{status={}}", status);
		this.status = status; 
	}
}

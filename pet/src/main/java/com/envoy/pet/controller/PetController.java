package com.envoy.pet.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.envoy.pet.model.Pet;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class PetController {
	
	private Integer status = HttpStatus.OK.value();
	
	
	@GetMapping(path = "/pets")
	public List<Pet> getPetStorePets(@RequestParam(required = true) String petStoreId) throws InterruptedException {
		
		log.info("/pets?petStoreId={}", petStoreId);
		
		List<Pet> pets = new ArrayList<Pet>();
		pets.add(new Pet("Mififú", 3));
		pets.add(new Pet("Toby", 5));
		
		/* Add two seconds delay for circuit breaking test purposes */
		Thread.sleep(2000);
		
		return pets;
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

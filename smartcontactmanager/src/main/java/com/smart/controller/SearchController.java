package com.smart.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entites.Contact;
import com.smart.entites.User;
import com.smart.service.SearchService;

@RestController
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String query,Principal p)
	{
		
		return searchService.searchData(query,p);
	}
	
}

package com.smart.service;

import java.security.Principal;

import org.springframework.http.ResponseEntity;

public interface SearchService {

	ResponseEntity <?> searchData(String query, Principal p); 

}

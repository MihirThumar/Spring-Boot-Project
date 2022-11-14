package com.smart.serviceImpl;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entites.Contact;
import com.smart.entites.User;
import com.smart.service.SearchService;

@Service
public class SearchServiceImpl  implements SearchService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;

	@Override
	public ResponseEntity <?> searchData(String query, Principal p) {
		System.out.println(query);
		User user = this.userRepository.getUserByUserName(p.getName());
		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
		
		
	}

}

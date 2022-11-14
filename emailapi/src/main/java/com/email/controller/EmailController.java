package com.email.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.email.model.EmailRequest;
import com.email.services.EmailService;

@RestController
public class EmailController {
	
	@Autowired
	private EmailService emailService;
	
	@GetMapping("/welcome")
	public String Welcome()
	{
		return "Email Api";
	}
	//gmail - axaybhimani@gmail.com
	//api to send email....
	@PostMapping("/sendemail")
	public  ResponseEntity<?> sendEmail(@RequestBody EmailRequest request)
	{
	
		System.out.println(request);
		boolean result = this .emailService.sendEmail(request.getSubject(),request.getTo(),request.getMessage());
		System.out.println(result);
		
		if(result)
		{
			return ResponseEntity.ok("Email is sent successfully....");
		}else {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Email Not Sent...");
		}
		
	}
	
}

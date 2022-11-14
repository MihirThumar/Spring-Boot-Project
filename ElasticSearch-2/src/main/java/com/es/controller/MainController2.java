package com.es.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.es.model.Student;
import com.es.service.ElasticService2;

@RestController
public class MainController2 {

	@Autowired
	private ElasticService2 service2;
	
	@PostMapping("/saveStu/{index}")
	public Object enrollStudent(@PathVariable String index,@RequestBody Student student,HttpSession httpSession) {
		
		Map<String, Object> addStudent = service2.addStudent(index, student);
		
		return addStudent;
	}
	
}

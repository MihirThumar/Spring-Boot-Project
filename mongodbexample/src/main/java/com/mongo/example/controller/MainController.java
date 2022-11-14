package com.mongo.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongo.example.model.Student;
import com.mongo.example.repo.StudentRepository;

@RestController
@RequestMapping("/student")
public class MainController {

	@Autowired
	private StudentRepository studentRepository;
	
	@PostMapping("/add")
	public ResponseEntity<?> addStudent(@RequestBody Student student){
		return ResponseEntity.ok(this.studentRepository.save(student));
	}
	
	@GetMapping("/view")
	public ResponseEntity<?> viewStudent(){
		return ResponseEntity.ok(this.studentRepository.findAll());
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> updateStudent(@RequestBody Student student){
		return ResponseEntity.ok(this.studentRepository.save(student));
	}
	
	@DeleteMapping("/delete/{id}")
	public Object deleteStudent(@PathVariable Integer id){
		
		this.studentRepository.deleteById(id);
		
		return id;
	}
	
}

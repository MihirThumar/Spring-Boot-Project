package com.mongo.example.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "students")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

	private int id;
	private String name;
	private String email;
	private String city;
	
}

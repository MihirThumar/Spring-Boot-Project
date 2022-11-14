package com.es.service;

import java.util.Map;

import com.es.model.Student;

public interface ElasticService2 {
	
	public Map<String,Object> addStudent(String index,Student student);
	
//	public Object dateQuery();
	
}

package com.es.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.http.ResponseEntity;

import com.es.model.Doctor;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ElasticService {

//	create index
	public Map<String,Object> addDoctor(String index,Doctor doc,HttpSession httpSession);
	
//	delete index
	public boolean deleteIndex(String index);
	
//	get all docotor
	public SearchResponse getDoctor(String index,String id,HttpSession httpSession) throws IOException;
	
//	get by id
	public Doctor getDoctorById(String index,String id,HttpSession httpSession);

//	meatch Search
	public List<Doctor> matchSearch(HttpSession httpSession);
	
//	deleteIndex
	public boolean deleteDoctorById(String index,String id);
	
//	Update the Doctor
	public UpdateResponse updateDoctorDetails(String index,String id,Doctor doctor,HttpSession httpSession) throws JsonProcessingException, IOException;
	
//	Aggreagation
	public ResponseEntity<?> aggregationFuction(String caseValue, String index);
	
	public ResponseEntity<?> cardinlityFunc();
	
//	Ranged Query
	public Object rangedQuery(Integer age,String index) throws IOException;
	
}

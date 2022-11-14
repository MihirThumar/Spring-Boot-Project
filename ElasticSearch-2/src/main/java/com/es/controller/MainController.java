package com.es.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.es.model.Doctor;
import com.es.service.ElasticService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class MainController {
	
	@Autowired
	private ElasticService service;
	
	@PostMapping("/save/{index}")
	public ResponseEntity<?> enrollDoctor(@PathVariable String index,@RequestBody Doctor doctor,HttpSession httpSession){
		
		service.addDoctor(index, doctor, httpSession);
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("respoonse", httpSession.getAttribute("rsponse"));
		map.put("status", true);
		map.put("code", 200);
		map.put("message", "succesfully enrooled student");
		return ResponseEntity.ok(map);
		
	}
	
	@DeleteMapping("deleteindex/{index}")
	public ResponseEntity<?> deleteIndexValue(@PathVariable String index){
		service.deleteIndex(index);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", true);
		map.put("code", 200);
		map.put("message", "deleted successfully with index : " + index);
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/view-doctor/{index}")
	public ResponseEntity<?> getDoctor(@PathVariable String index, HttpSession httpSession) throws IOException{
		service.getDoctor(index, index, httpSession);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("response", httpSession.getAttribute("response"));
		map.put("status", true);
		map.put("code", 200);
		map.put("message", "successfully got students list");
		
		return ResponseEntity.ok(map);

	}
	
	@GetMapping("/view-doctor-by-id/{index}/{id}")
	public ResponseEntity<?> getDoctorById(@PathVariable String id,@PathVariable String index,HttpSession httpSession){
		
		service.getDoctorById(index, id, httpSession);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("response", httpSession.getAttribute("doctorWithId"));
		map.put("status", true);
		map.put("code", 200);
		map.put("message", "successfully got doctor");
		service.matchSearch(httpSession);
		map.put("boolQuery", httpSession.getAttribute("boolQueryResult"));
		return ResponseEntity.ok(map);
	}
	
	@DeleteMapping("/deleteDocoto/{index}/{id}")
	public ResponseEntity<?> deleteDoctor(@PathVariable String index, @PathVariable String id){
	
		service.deleteDoctorById(index, id);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", true);
		map.put("code", 200);
		map.put("message", "deleted Doctor successfully  : " + index + " and id is : " + id);
		return ResponseEntity.ok(map);
	}
	
	@PutMapping("/updateIndexValue/{index}/{id}")
	public ResponseEntity<?> updateDoctor(@RequestBody Doctor doctor, @PathVariable String index, @PathVariable String id, HttpSession httpSession) throws JsonProcessingException, IOException{
	
		service.updateDoctorDetails(index, id, doctor, httpSession);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("response", httpSession.getAttribute("updateRes"));
		map.put("status", true);
		map.put("code", 200);
		map.put("message", "updated Doctor successfully with index : " + index);
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/aggregation/{index}/{caseValue}")
	public ResponseEntity<?> aggregationFun(@PathVariable String caseValue, @PathVariable String index){
		Map<String,Object> map = new HashMap<>();
		map.put("response", service.aggregationFuction(caseValue, index).getBody());
		map.put("code", service.aggregationFuction(caseValue, index).getStatusCodeValue());
		map.put("status", service.aggregationFuction(caseValue, index).getStatusCode());
		map.put("message", "Got aggergation record succesFully");
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/cardinality")
	public ResponseEntity<?> cardnality() {
		Map<String, Object> map = new HashMap<>();
		service.cardinlityFunc();
		map.put("response", service.cardinlityFunc().getBody());
		map.put("code", service.cardinlityFunc().getStatusCodeValue());
		map.put("status", service.cardinlityFunc().getStatusCode());
		map.put("message", "Got cardinality record successfully");
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/range/{index}/{age}")
	public Object rangedQueryimpl(@PathVariable Integer age,@PathVariable String index) throws IOException {
		Object rangedQuery = service.rangedQuery(age, index);
		return rangedQuery;
	}
	
}

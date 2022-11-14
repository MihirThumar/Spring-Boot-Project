package com.school.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.dao.SchoolDao;
import com.school.model.School;
import com.school.model.SchoolModel;
import com.school.model_input.School_Input;
import com.school.services.SchoolService;
import com.school.system.RestResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class Controller {

	@Autowired
	private SchoolDao schoolDao;

	@Autowired
	private SchoolService schoolService;

	@GetMapping("/school")
	@ApiOperation(value = "Finds All Schools")
	public ResponseEntity<?> getSchool(HttpSession httpSession) throws JsonProcessingException {
		Object school = this.schoolService.getSchool(httpSession);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("response", school);
		map.put("code", 200);
		map.put("stauts", "true");
		map.put("message", "Got Response from mysql Database SuccesFully");

		return ResponseEntity.ok(map);
	}

//	@GetMapping("/sss")
//	public Object getingSchool() throws FileNotFoundException	{
//		InputStream inputStream = new FileInputStream(new File("C:\\Users\\mihir\\Desktop\\Properties\\properties.yml"));
//		Yaml yaml = new Yaml();
//
//		Map<String , Object> map =  yaml.load(inputStream);
//
//		return map;
//	}

	@GetMapping("/school/{schoolId}")
	@ApiOperation(value = "Finds School By Id", notes = "Provide an id to look up specific School from the address book", response = School.class)
	public ResponseEntity<?> getSchool(
			@ApiParam(value = "Id value for the school you need to retrieve", required = true) @PathVariable String schoolId,
			HttpSession httpSession) {

		Object school = this.schoolService.getSchool(Integer.parseInt(schoolId));
//		System.out.println(school);
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("response", school);
		map.put("code", 200);
		map.put("stauts", "true");
		map.put("message", "Got Response from mysql Database SuccesFully");

		return ResponseEntity.ok(map);
	}

	@PostMapping("/school")
	public ResponseEntity<?> enrolSchool(@RequestBody School_Input input, HttpSession httpSession) {

		this.schoolService.addSchool(input, httpSession);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("response", httpSession.getAttribute("response"));
		map.put("stauts", true);
		map.put("code", 200);
		map.put("message", "succesfully enroled School");

		return ResponseEntity.ok(map);
	}

	@PutMapping("/school")
	public ResponseEntity<?> updateSchool(@RequestBody School school, HttpSession httpSession) {

		Object updateSchool = this.schoolService.updateSchool(school);

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("response", updateSchool);
		map.put("stauts", true);
		map.put("code", 200);
		map.put("message", "succesfully enroled School");

		return ResponseEntity.ok(map);
	}

	@DeleteMapping("/school/{id}")
	public ResponseEntity<?> deleteSchool(@PathVariable String id) {

		schoolService.deleteSchool(Integer.parseInt(id));

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("stauts", true);
		map.put("code", 200);
		map.put("message", "succesfully Delete School By Id " + id);

		return ResponseEntity.ok(map);
	}

	@GetMapping("/ss")
	public Object getAllSchool() {
		List<School> allUser = this.schoolDao.getAllUser();
		return allUser;
	}

	@GetMapping("/view-by-name-city/{name}/{city}")
	public Object getByName(@PathVariable String name, @PathVariable String city,HttpSession httpSession) {
			Object schoolByNameAndCity = schoolService.getSchoolByNameAndCity(name, city);
			return schoolByNameAndCity;
	}

	@GetMapping("/listbyname")
	public RestResponse getListByName(@RequestBody SchoolModel schoolModel) {
		
		return  schoolService.getSchoolListBySchoolName(schoolModel);
			
	
	}

}

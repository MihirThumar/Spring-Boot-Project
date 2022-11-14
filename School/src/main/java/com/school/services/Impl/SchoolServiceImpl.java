package com.school.services.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.dao.SchoolDao;
import com.school.model.School;
import com.school.model.SchoolModel;
import com.school.model_input.School_Input;
import com.school.services.SchoolService;
import com.school.system.RestResponse;

@Service
public class SchoolServiceImpl implements SchoolService {

	@Autowired
	private SchoolDao dao;

//Get All School	
	@Override
	public Object getSchool(HttpSession httpSession) throws JsonProcessingException {
		List<School> list = dao.findAll();
		List<School_Input> list2 = new ArrayList<>();
		for (School school : list) {
			School_Input school_Input = new School_Input();

			school_Input.setCity(school.getCity());
			school_Input.setDistrict(school.getDistrict());
			school_Input.setPinode(school.getPinode());
			school_Input.setSchoolName(school.getSchoolName());

			ObjectMapper mapper = new ObjectMapper();
			JsonNode readTree = mapper.readTree(school.getStudent_List());

			school_Input.setStudent_List(readTree);

			list2.add(school_Input);
		}
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("response", list2);

		return list2;
	}

//Get School By id
	@SuppressWarnings("deprecation")
	@Override
	public Object getSchool(Integer schoolId) {

		try {
			School school = dao.getById(schoolId);
//			session.setAttribute("response", list);
			return school;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

	}

//Adding School
	@Override
	public Map<String, Object> addSchool(School_Input input, HttpSession session) {

		School school = new School();
		school.setSchoolName(input.getSchoolName());
		school.setCity(input.getCity());
		school.setDistrict(input.getCity());
		school.setPinode(input.getPinode());
		school.setStudent_List(input.getStudent_List().toString());

		try {
			dao.save(school);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("id", school.getId());
		map.put("schoolName", school.getSchoolName());
		map.put("city", school.getCity());
		map.put("pincode", school.getPinode());
		map.put("district", school.getDistrict());
		map.put("studentList", school.getStudent_List());

		session.setAttribute("response", map);

		return map;
	}

//Updating School
	@Override
	public Object updateSchool(School school) {
		try {
			dao.save(school);
			return school;
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

//Deleting School
	@Override
	public School deleteSchool(int schoolId) {
		School school = dao.getById(schoolId);
		dao.delete(school);
		return school;
	}

//Get School By Name and City
	@Override
	public Object getSchoolByNameAndCity(String name, String city) {

		List<School> schoolBySchoolName = null;
		try {
			schoolBySchoolName = this.dao.getSchoolBySchoolName(name, city);
			if (schoolBySchoolName.size() <= 0) {
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("response", "something get wrong !!");
				map.put("status", false);
				map.put("code", 404);
				map.put("message", "Please provide valid information ");

				return ResponseEntity.ok(map);
			}

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("response", schoolBySchoolName);
			map.put("stauts", true);
			map.put("code", 200);
			map.put("message", "succesfully get School By name and city ");

			return schoolBySchoolName;
		} catch (Exception e) {
			e.printStackTrace();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("response", "something get wrong !!");
			map.put("status", false);
			map.put("code", 404);
			map.put("message", "Please provide valid information ");

			return ResponseEntity.ok(map);
		}

	}

//Getting Schoo By List
	@Override
	public RestResponse getSchoolListBySchoolName(SchoolModel model) {
		List<School> list = null;
		try {
			list = this.dao.findBySchoolName(model.getNameList());
			if (list.size() <= 0) {
				return new RestResponse().setCode(404).setStauts(false).setMessage("Please provide valid information").setData(HttpStatus.NOT_FOUND);
			}
			return new RestResponse().setCode(200).setStauts(true).setMessage("Get Data Succesfully").setData(list);
		
		} catch (Exception e) {
			e.printStackTrace();
//			return new RestResponse("Please provide valid information", false, 404, e.getMessage()); 
			return new RestResponse().setCode(404).setStauts(false).setMessage("Not Found").setData(HttpStatus.NOT_FOUND);
		}
	}

}

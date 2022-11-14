package com.school.services;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.school.model.School;
import com.school.model.SchoolModel;
import com.school.model_input.School_Input;
import com.school.system.RestResponse;

public interface SchoolService {

	public Object getSchool(HttpSession session) throws JsonProcessingException;
	
	public Object getSchool(Integer schoolId);

	public Map<String, Object> addSchool(School_Input input,HttpSession session);
	
	public Object updateSchool(School school);

	public School deleteSchool(int schoolId);
	
	public Object getSchoolByNameAndCity(String name,String city);
	
	RestResponse getSchoolListBySchoolName(SchoolModel model);
	
}

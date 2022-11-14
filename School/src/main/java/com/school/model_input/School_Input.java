package com.school.model_input;

import com.fasterxml.jackson.databind.JsonNode;

public class School_Input {

	private String schoolName;
	private String city;
	private String district;
	private int pinode;
	private JsonNode student_List;
	
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public int getPinode() {
		return pinode;
	}
	public void setPinode(int pinode) {
		this.pinode = pinode;
	}
	public JsonNode getStudent_List() {
		return student_List;
	}
	public void setStudent_List(JsonNode student_List) {
		this.student_List = student_List;
	}
	
	
}
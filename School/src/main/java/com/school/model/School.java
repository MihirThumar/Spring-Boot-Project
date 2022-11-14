package com.school.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@ApiModel(description = "Details about the School")
public class School {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(notes = "The unique id for the school")
	private int id;
	
	@ApiModelProperty(notes = "The school's name")
	private String schoolName;
	
	@ApiModelProperty(notes = "The school's city")
	private String city;
	
	@ApiModelProperty(notes = "The school's District")
	private String district;
	private int pinode;
	
	@ApiModelProperty(notes = "The school's student list")
	private String student_List;
	
	public String getStudent_List() {
		return student_List;
	}
	public void setStudent_List(Object object) {
		this.student_List = (String) object;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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


	
}

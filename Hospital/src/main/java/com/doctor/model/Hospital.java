package com.doctor.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Hospital {

	@Id 
	private int id;
	private String name;
	private String city;
	private String doctorList;
	
}

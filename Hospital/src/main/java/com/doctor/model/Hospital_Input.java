package com.doctor.model;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Hospital_Input {

	@ApiModelProperty(value = "The Unique id for Each Hospital")
	private int id;
	
	@ApiModelProperty(value = "Name of Hospital")
	private String name;
	
	@ApiModelProperty(value = "Name of City")
	private String city;
	
	@ApiModelProperty(value = "Pass the list of Doctros who works in Hospital")
	private JsonNode doctorList;
	
}

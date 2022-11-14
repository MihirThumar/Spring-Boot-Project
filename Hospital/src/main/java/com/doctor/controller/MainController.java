package com.doctor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.doctor.model.Hospital;
import com.doctor.model.HospitalNameList;
import com.doctor.model.Hospital_Input;
import com.doctor.service.HospitalService;
import com.doctor.system.RestResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;

@RestController
public class MainController {

	@Autowired
	private HospitalService service;

	@PostMapping("/add-hospital") 	@ApiOperation(value = "Add Hospital with Doctros")
	public Object add(@RequestBody Hospital_Input hospital) {
		try {
			return service.addHospital(hospital);
		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponse().setCode(404).setStatus(false).setMessage(e.getMessage())
					.setData("Something Wrong in sending request try sometime later !!");
		}
	}

	@GetMapping("/view")@ApiOperation(value = "Get All hospital")
	public Object getAll() {
		try {
			return service.getAllHospital();
		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponse().setCode(404).setStatus(false).setMessage(e.getMessage())
					.setData("Something Wrong in sending request try sometime later !!");
		}
	}

	@GetMapping("/view/{id}") @ApiOperation( value = "Get Hospital By Id")
	public Object getById(@PathVariable Integer id) {
		try {
			return service.getHospital(id);
		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponse().setCode(404).setStatus(false).setMessage(e.getMessage())
					.setData("Something Wrong in sending request try sometime later !!");
		}
	}

	@PutMapping("/update") @ApiOperation(value = "update value of Hospital")
	public Object updateHospital(@RequestBody Hospital_Input hospital) {
		try {
			return service.updateHospital(hospital);
		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponse().setCode(404).setStatus(false).setMessage(e.getMessage())
					.setData("Something Wrong in sending request try sometime later !!");
		}
	}

	@DeleteMapping("/delete/{id}") @ApiOperation(value = "Delete Hospital By id")
	public Object deleteHospital(@PathVariable Integer id) {
		try {
			return service.deleteHospitla(id);
		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponse().setCode(404).setStatus(false).setMessage(e.getMessage())
					.setData("Something Wrong in sending request try sometime later !!");
		}
	}

	@GetMapping("/view_by_list") @ApiOperation(value = "Get Hospital By Name",notes = "You have to pass list of hospital name")
	public Object getHospitalByList(@RequestBody HospitalNameList list) {
		try {
			return service.getHospitalByName(list);
		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponse().setCode(404).setStatus(false).setMessage(e.getMessage())
					.setData("Something Wrong in sending request try sometime later !!");
		}
	}
	
	@GetMapping("/view_by_name_city")
	public Object getHospitalByNameAndCity(@RequestBody HospitalNameList list) {
		try {
			
			return service.getHospitalByNameAndCity(list);
		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponse().setCode(404).setStatus(false).setMessage(e.getMessage())
					.setData("Something Wrong in sending request try sometime later !!");
		}
	}
	
}

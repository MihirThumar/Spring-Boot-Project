package com.doctor.service;

import java.util.List;

import com.doctor.model.HospitalNameList;
import com.doctor.model.Hospital_Input;

public interface HospitalService {

	public Object addHospital(Hospital_Input input);
	
	public Object getAllHospital();
	
	public Object getHospital(Integer id);
	
	public Object updateHospital(Hospital_Input hospital);
	
	public Object deleteHospitla(Integer id);
	
	public Object getHospitalByName(HospitalNameList list);
	
	public Object getHospitalByNameAndCity(HospitalNameList list);
	
}

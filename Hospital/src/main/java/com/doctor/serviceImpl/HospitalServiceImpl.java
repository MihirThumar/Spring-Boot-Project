package com.doctor.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doctor.dao.HospitalRepositiry;
import com.doctor.model.Hospital;
import com.doctor.model.HospitalNameList;
import com.doctor.model.Hospital_Input;
import com.doctor.service.HospitalService;
import com.doctor.system.RestResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class HospitalServiceImpl implements HospitalService {

	@Autowired
	private HospitalRepositiry repositiry;

	ObjectMapper mapper = new ObjectMapper();

//	Enrolle New Hospital
	@Override
	public Object addHospital(Hospital_Input input) {
		Hospital hospital = new Hospital();
		hospital.setId(input.getId());
		hospital.setName(input.getName());
		hospital.setCity(input.getCity());
		hospital.setDoctorList(input.getDoctorList().toString());

		try {
			Hospital save = repositiry.save(hospital);
			return new RestResponse().setCode(202).setStatus(true).setMessage("succesFully stored").setData(save);
		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponse().setCode(404).setStatus(false).setMessage(e.getMessage())
					.setData("Something went wrong try again sometime later");
		}
	}

//	Get All School
	@Override
	public Object getAllHospital() {
		try {
			List<Hospital> findAll = repositiry.findAll();
			List<Hospital_Input> list = new ArrayList<>();

			for (Hospital h : findAll) {
				Hospital_Input input = new Hospital_Input();
				input.setId(h.getId());
				input.setCity(h.getCity());
				input.setName(h.getName());

				String doctorList = h.getDoctorList();
				JsonNode readTree = mapper.readTree(doctorList);

				input.setDoctorList(readTree);
				list.add(input);
			}
			return new RestResponse().setCode(202).setStatus(true).setMessage("succesFully Got From Database")
					.setData(list);

		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponse().setCode(404).setStatus(false).setMessage(e.getMessage())
					.setData("Something went wrong try again sometime later");
		}
	}

//	Get Hospital by id
	@Override
	public Object getHospital(Integer id) {

		try {
			Optional<Hospital> findById = repositiry.findById(id);
			return new RestResponse().setCode(202).setStatus(true).setMessage("succesFully Got From Database")
					.setData(findById);
		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponse().setCode(404).setStatus(false).setMessage(e.getMessage())
					.setData("Something went wrong try again sometime later");
		}
	}

//Update Hospital
	@Override
	public Object updateHospital(Hospital_Input hospital) {

		try {
			Hospital h2 = new Hospital();
			h2.setCity(hospital.getCity());
			h2.setDoctorList(hospital.getDoctorList().toString());
			h2.setId(hospital.getId());
			h2.setName(hospital.getName());
			Hospital save = repositiry.save(h2);
			return new RestResponse().setCode(202).setStatus(true)
					.setMessage("succesFully Update Hospital and Doctor List").setData(save);
		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponse().setCode(404).setStatus(false).setMessage(e.getMessage())
					.setData("Something went wrong try again sometime later");
		}
	}

//Delete The Hospital
	@Override
	public Object deleteHospitla(Integer id) {
		try {
			repositiry.deleteById(id);
			return new RestResponse().setCode(202).setStatus(true)
					.setMessage("succesFully Delete Hospital and Doctor List").setData("Delete Hospital by id " + id);
		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponse().setCode(404).setStatus(false).setMessage(e.getMessage())
					.setData("Something went wrong try again sometime later");
		}
	}

//get Hospital by Name List
	@Override
	public Object getHospitalByName(HospitalNameList list) {

		try {
			List<Hospital> findByHospitalName = repositiry.findByHospitalName(list.getNameList());
			if (findByHospitalName.size() <= 0) {
				return new RestResponse().setCode(400).setStatus(false)
						.setMessage("There was no Hospital with this nameList")
						.setData("Something went wrong try again sometime later");
			}
			return new RestResponse().setCode(202).setStatus(true).setMessage("succesFully get Hospital by Name List")
					.setData(findByHospitalName);

		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponse().setCode(404).setStatus(false).setMessage(e.getMessage())
					.setData("Something went wrong try again sometime later");
		}
	}

//get Hospital by name and city
	@Override
	public Object getHospitalByNameAndCity(HospitalNameList list) {
		try {
			
			List<Hospital> findByHospitalNameAndCity = repositiry.findByHospitalNameAndCity(list.getNameList(), list.getCityList());
			if (findByHospitalNameAndCity.size() <= 0) {
				return new RestResponse().setCode(400).setStatus(false)
						.setMessage("No match with name and city")
						.setData("Something went wrong try again sometime later");
			}
			return new RestResponse().setCode(202).setStatus(true).setMessage("succesFully get Hospital by Name List")
					.setData(findByHospitalNameAndCity);
		} catch (Exception e) {
			e.printStackTrace();
			return new RestResponse().setCode(404).setStatus(false).setMessage(e.getMessage())
					.setData("Something went wrong try again sometime later");
		}
	}
}

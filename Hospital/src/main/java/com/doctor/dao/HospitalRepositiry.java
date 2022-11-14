package com.doctor.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.doctor.model.Hospital;

public interface HospitalRepositiry extends JpaRepository<Hospital, Integer> {

	@Query(value = "select h from Hospital h where h.name IN ?1")
	public List<Hospital> findByHospitalName(List<String> list);
	
	@Query(value = "select h from Hospital h where h.name IN ?1 and h.city IN ?2")
	public List<Hospital> findByHospitalNameAndCity(List<String> name,List<String> city);
	
}

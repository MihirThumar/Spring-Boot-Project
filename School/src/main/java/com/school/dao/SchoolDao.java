package com.school.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import com.fasterxml.jackson.databind.JsonNode;
import com.school.model.School;

//@EnableJpaRepositories
public interface SchoolDao extends JpaRepository<School, Integer> {

	@Query("select s from School s")
	public List<School> getAllUser();
	
	@Query("select s from School s where s.schoolName = ?1 and s.city =?2")
	public List<School> getSchoolBySchoolName(String name, String city);
	
	@Query("select s from School s where city=?1 order by school_name")
	public List<School> findBySchoolSorted(String city);
	
	@Query(value = "select s from School s where  s.schoolName IN  ?1")
	public List<School> findBySchoolName( List<String> list);
	
}

package com.capgemini.piapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.piapi.domain.Developer;

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long>{
	
	Developer findByDevId(String developerIdentifier);
	public List<Developer> findAll();
	
}
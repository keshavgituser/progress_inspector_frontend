package com.capgemini.piapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.piapi.domain.Developer;

/**
 * This Developer will be responsible to perform all Developer related CRUD operations  
 * on Developer
 * @author Harsh Joshi
 *
 */

@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

	/**
	 * This will find Developer on basis of the developer identifier
	 * @param developerIdentifier
	 * @return developer according to developerIdentifier if developerIdentifier found
	 */
	Developer findByDevId(String developerIdentifier);
	
	
	/**
	 * This method will list all the developers available
	 */
	public List<Developer> findAll();

}
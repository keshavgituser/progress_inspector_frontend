package com.capgemini.piapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.piapi.domain.ProductOwner;

/**
 * This ProductOwnerRepository will be responsible for performing Product Owner related CRUD operations 
 * on  ProductOwner
 * @author Aadesh Juvekar
 *
 */
@Repository
public interface ProductOwnerRepository extends JpaRepository<ProductOwner, Long> {
	
	/**
	 * This deleteByLoginName method will delete the ProductOwner by loginName from the database.
	 * @param loginName of ProductOwner to be deleted.
	 */
	public void deleteByLoginName(String loginName);
	/**
	 * This findByLoginName method will return the ProductOwner by loginName from the database.
	 * 
	 * @param loginName of ProductOwner to be found.
	 * @return ProductOwner if found otherwise null.
	 */
	public ProductOwner findByLoginName(String loginName);
	
	/**
	 * This findByLoginName method will return the ProductOwner by loginName and password from the database.
	 * 
	 * @param loginName of ProductOwner to be found.
	 * @param pwd of ProductOwner to be found.
	 * @return ProductOwner if found otherwise null.
	 */
	public ProductOwner findByLoginNameAndPwd(String loginName, String pwd);

}

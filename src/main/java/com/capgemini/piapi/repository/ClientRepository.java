package com.capgemini.piapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.piapi.domain.Client;

/**
 * This ClientRepository will be responsible for performing Client related CRUD
 * operations on Client
 * 
 * @author Tejas Naik
 *
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	Client findByLoginName(String loginName);

	Client findByLoginNameAndPwd(String loginName, String pwd);
}

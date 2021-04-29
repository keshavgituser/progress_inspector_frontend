package com.capgemini.piapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.piapi.domain.Client;

@Repository
public interface ClientRepository  extends JpaRepository<Client,Long>{

	Client findByLoginName(String loginName);
}

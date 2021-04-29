package com.capgemini.piapi.serviceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.domain.Client;
import com.capgemini.piapi.repository.ClientRepository;
import com.capgemini.piapi.service.ClientService;

/**
 * This ClientServiceImpl implements the business logic on the database.
 * @author tejas
 *
 */
@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepository;


	@Override
	public Client getClientById(Long Id) {
		// TODO Auto-generated method stub
		return clientRepository.findByid(Id);
	}

}

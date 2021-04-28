package com.capgemini.piapi.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.domain.Client;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.repository.ClientRepository;
import com.capgemini.piapi.repository.TaskRepository;
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
	
	@Autowired
	private TaskRepository taskRepository;
	
	
	private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

	@Override
	public Client getClientById(Long Id) {
		// TODO Auto-generated method stub
		return clientRepository.findByid(Id);
	}

}

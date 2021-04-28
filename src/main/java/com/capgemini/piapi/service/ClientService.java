package com.capgemini.piapi.service;

import com.capgemini.piapi.domain.Client;

public interface ClientService {
	
	/**
	 * This function will get client by client's id
	 * @param client
	 * @return client if available
	 */
	public Client getClientById(Long id);

}

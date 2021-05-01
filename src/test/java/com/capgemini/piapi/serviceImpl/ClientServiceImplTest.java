package com.capgemini.piapi.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;

import com.capgemini.piapi.domain.Client;
import com.capgemini.piapi.exception.ClientPassedNullException;
import com.capgemini.piapi.exception.ClientNotFoundException;
import com.capgemini.piapi.repository.ClientRepository;
import com.capgemini.piapi.service.ClientService;

@SpringBootTest
class ClientServiceImplTest {
	
	@Autowired
	MockHttpSession session;
	
	@Autowired
	ClientService clientService;

	@Autowired
	ClientRepository clientRepository;
	
	/**
	 *
	 * This Test Checks saveClient Method for ClientName as null Input
	 *  @author Keshav
	 */
	@Test
	void test_saveClient_Given_Only_Client_Name_Should_ThrowClientPassedNullException() 
	{
		Client client=new Client();
		client.setClientName("New Userfirst");
//		client.setLoginName("New Loginfirst");
//		client.setPwd("New passfirst");
		assertThrows(ClientPassedNullException.class,()-> clientService.saveClient(client));
		clientRepository.delete(client);
		
		
	}
	
	/**
	 * This Test Checks saveClient Method for loginName as null Input
	 *  @author Keshav
	 */
	
	@Test
	void test_saveClient_Given_Only_Client_loginName_Should_ThrowClientPassedNullException() 
	{
		Client client=new Client();
		//client.setClientName("New Usersecond");
		client.setLoginName("Loginsecond");
//		client.setPwd("passsecond");
		assertThrows(ClientPassedNullException.class,()-> clientService.saveClient(client));
		clientRepository.delete(client);
		
		
	}
	/**
	 * This Test Checks saveClient Method for Password as null Input
	 *  @author Keshav
	 */
	@Test
	void test_saveClient_Given_Only_Client_Password_Should_ThrowClientPassedNullException() 
	{
		Client client=new Client();
		//client.setClientName("New Userthird");
		//client..setLoginName("Loginthird");
		client.setPwd("passthird");
		assertThrows(ClientPassedNullException.class,()-> clientService.saveClient(client));
		clientRepository.delete(client);
		
	}
	/**
	 * This Test Checks saveClient Method for Returning SavedClient
	 *  @author Keshav
	 */
	@Test
	void test_saveClient_Given_Client_Should_Return_The_Saved_Client() 
	{
		Client client=new Client();
		client.setClientName("NameOne");
		client.setLoginName("LoginOne");
		client.setPwd("passOne");
		Client savedClient=clientService.saveClient(client);
		assertEquals(client,savedClient);
		clientRepository.delete(client);
	}
	/**
	 * This Test Checks findByLoginName Method for loginName as null Input
	 *  @author Keshav
	 */
	@Test
	void test_findByLoginName_Given_String_LoginName_ThrowsClientPassedNullException()
	{
		String loginName=null;
		
		assertThrows(ClientPassedNullException.class,()-> clientService.findByLoginName(loginName));
	}
	/**
	 * This Test Checks findByLoginName Method for Returning foundClient using loginName
	 *  @author Keshav
	 */
	@Test
	void test_findByLoginName_given_String_loginName_Return_Client()
	{
		Client client=new Client();
		client.setClientName("NameTwo");
		client.setLoginName("LoginTwo");
		client.setPwd("passTwo");
		Client savedClient2=clientRepository.save(client);
		Client foundclient=clientService.findByLoginName("LoginTwo");

		assertEquals(savedClient2.getLoginName(),foundclient.getLoginName());
		clientRepository.delete(client);
		
	}
	/**
	 * This Test Checks deleteByLoginName Method for valid loginName as Input
	 *  @author Keshav
	 */
	@Test
	void test_deleteClientByLoginName_Given_String_loginName_Removes_Client()
	{
		Client client=new Client();
		client.setClientName("NameThree");
		client.setLoginName("LoginThree");
		client.setPwd("passThree");
		Client savedClient3=clientRepository.save(client);
		clientService.deleteClientByLoginName("LoginThree");
		
		assertEquals(null,clientService.findByLoginName("LoginThree"));
		clientRepository.delete(client);
	}
	/**
	 * This Test Checks Whether Client  is present to delete if not present
	 * Throws ClientNotFoundException
	 */
	@Test
	void test_deleteClientByLoginName_Given_String_loginName_Throw_ClientNotFoundException()
	{
		assertThrows(ClientNotFoundException.class,()->clientService.deleteClientByLoginName(null));
		
	}
	@Test
	void test_authenticateClient_Given_String_loginName_String_Password_Return_User()
	{
		Client client = new Client();
		client.setClientName("Test Client");
		client.setLoginName("Test1");
		client.setPwd("Test1");
		Client savedClient = clientService.saveClient(client); 
		
		clientService.authenticateClient(savedClient.getLoginName(), savedClient.getPwd(), session);
		
		assertEquals(client.getLoginName(),session.getAttribute("loginName"));
		
		clientRepository.delete(client);	
		session.invalidate();		
	}
	@Test
	void test_authenticateClient_Given_String_loginName_String_Password_Return_ClientNotFoundException()
	{

		assertThrows(ClientNotFoundException.class, ()->clientService.authenticateClient("Test2", "Test2", session));	
	}
	/**
	 * This Test Checks authenticataClient Method for loginName and Password as null Input
	 *  @author Keshav
	 */
	@Test
	void test_authenticateClient_Given_null_loginName_null_Password_Return_ClientNotFoundException()
	{

		assertThrows(ClientNotFoundException.class, ()->clientService.authenticateClient(null, null, session));	
	}
	/**
	 * This Test Checks findallClients Method ThrowsClientNotFoundException
	 * Incase No Clients are there in database
	 *  @author Keshav
	 */
	@Test
	void test_findAllClients_ThrowsClientNotFoundException_If_ClientsList_IsEmpty()
	{
		assertThrows(ClientNotFoundException.class,()->clientService.getAllClients());
	}
	/**
	 * This Test Checks updateClient given null Throws CLientNotFoundException
	 *  @author Keshav
	 */
	@Test
	void test_updateClient_null_Client_Throw_ClientNotFoundException()
	{
		Client client=new Client();
		assertThrows(ClientNotFoundException.class,()-> clientService.updateClient(client));
	}
	/**
	 * This Test Checks updateClient given new ClientName and Password Return Updated Client
	 *  @author Keshav
	 */
	@Test
	void test_updateClient_Client_Returns_Updated_Client()
	{
		Client client=new Client();
		client.setClientName("NameFive");
		client.setLoginName("LoginFive");
		client.setPwd("passFive");
		Client savedClient=clientService.saveClient(client);
		assertEquals("LoginFive",savedClient.getLoginName());
		client.setClientName("NameSix");
		client.setPwd("passSix");
		Client updatedClient=clientService.updateClient(client);
		assertEquals("NameSix",updatedClient.getClientName());
		
		clientRepository.delete(client);
		
	}
	
	
		
	
	

}

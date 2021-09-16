package com.capgemini.piapi.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Service;

import com.capgemini.piapi.domain.Client;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.ClientPassedNullException;
import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.exception.ClientNotFoundException;
import com.capgemini.piapi.repository.ClientRepository;
import com.capgemini.piapi.repository.ProductOwnerRepository;
import com.capgemini.piapi.service.ClientService;
import com.capgemini.piapi.service.ProductOwnerService;

@SpringBootTest
class ClientServiceImplTest {
	
	@Autowired
	MockHttpSession session;
	
	@Autowired
	ClientService clientService;

	@Autowired
	ClientRepository clientRepository;
	
	/**
	 * ProductOwner service Is Used To Test Tasks assigned to Client
	 */
	@Autowired
	private ProductOwnerService productOwnerService;
	/**
	 * 
	 *These are the test cases related to viewTask method in the client service
	 *	View task Successful
	 */
			/*
			 * Uncomment this Test When The Product owner has added client to task
			@Test
			public void test_viewTask_GivenClientLoginNameAndTaskIdentifier_ShouldReturnTask() {
				
				Client client=new Client();
				client.setName("NameTry");
				client.setLoginName("LoginTry");
				client.setPwd("PasswordTry");
				
				clientService.saveClient(client);
				
				Task task = new Task();
				task.setDescription("task");
				task.setProgress("tsk");
				task.setTaskIdentifier("taskid");
				task.setTitle("task");
				
				productOwnerService.addTaskToClient("LoginTry","taskid");
				Task task1 = clientService.viewTask("LoginTry","taskid");
				
				assertEquals(task.getDescription(), task1.getDescription());	
				assertEquals(task.getProgress(), task1.getProgress());
				assertEquals(task.getTaskIdentifier(), task1.getTaskIdentifier());
				assertEquals(task.getTitle(), task1.getTitle());
				
				clientRepository.delete(client);
				
				
			}
			*/

			//View task Unsuccessful because Invalid Task identifier
			@Test
			public void test_viewTask_GivenClientrLoginNameandInvalidTaskIdentifier_ShouldThrowClientNotFoundException() {
				assertThrows(ClientNotFoundException.class, () -> clientService.viewTask("client", null));
			}
			
			//View task Unsuccessful because Invalid Client login Name
			@Test
			public void test_viewTask_GivenInvalidClientLoginNameAndValidTaskIdentifier_ShouldThrowTaskIdException() {
				assertThrows(ClientNotFoundException.class, () -> clientService.viewTask("clint", "task"));
			}

			//View task Unsuccessful because Empty Task identifier
			@Test
			public void test_viewTask_GivenClientrLoginNameandEmptyTaskIdentifier_ShouldThrowClientNotFoundException() {
				assertThrows(ClientNotFoundException.class, () -> clientService.viewTask("client",null));
			}
			
			//View task Unsuccessful because Empty Client login Name
			@Test
			public void test_viewTask_GivenEmptyClientLoginNameAndValidTaskIdentifier_ShouldThrowTaskIdException() {
				assertThrows(ClientNotFoundException.class, () -> clientService.viewTask(null, "task"));
			}
	
	
	
	
	/**Client Test Cases
	 *
	 * This Test Checks saveClient Method for ClientName as null Input
	 *  @author Keshav
	 */
	@Test
	void test_saveClient_Given_Only_Client_Name_Should_ThrowClientPassedNullException() 
	{
		Client client=new Client();
		client.setName("New Userfirst");
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
		//client.setName("New Usersecond");
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
		//client.setName("New Userthird");
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
		client.setName("NameOne");
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
		client.setName("NameTwo");
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
		client.setName("NameThree");
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
		client.setName("Test Client");
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
		clientRepository.deleteAll();
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
		client.setName("NameFive");
		client.setLoginName("LoginFive");
		client.setPwd("passFive");
		Client savedClient=clientService.saveClient(client);
		assertEquals("LoginFive",savedClient.getLoginName());
		client.setName("NameSix");
		client.setPwd("passSix");
		Client updatedClient=clientService.updateClient(client);
		assertEquals("NameSix",updatedClient.getName());
		
		clientRepository.delete(client);
		
	}
	
	
	/**
	 * Task Test Cases
	 */
	//Test cases related to addRemark method in the client service
		//Add remark Successfully
		/*
		@Test
		public void test_addRemark_GivenRemark_ShouldReturnSavedRemark() {
			Remark remark = new Remark();
			remark.setDescription("Test Owner");
			remark.setGivenBy("Test");
			Remark savedremark = clientService.addRemark(remark, "task");
			assertEquals(remark, savedremark);
		}
		*/
		//Add remark Unsuccessful because Invalid Task id
		@Test
		public void test_addRemark_GivenRemarkWithInvalidTaskIdentifier_ShouldThrowTaskIdException() {
			Remark remark = new Remark();
			remark.setGivenBy("remark");
			remark.setDescription("Test Owner");
			assertThrows(TaskIdException.class, () -> clientService.addRemark(remark, "task1"));
		}
		
		//Add remark Unsuccessful because Empty Task identifier
		@Test
		public void test_addRemark_GivenRemarkWithEmptyTaskIdentifier_ShouldThrowTaskIdException() {
			Remark remark = new Remark();
			remark.setGivenBy("remark");
			remark.setDescription("Test owner");
			assertThrows(TaskIdException.class, () -> clientService.addRemark(remark, ""));
		}
		
		
		
	
	
		
	
	

}

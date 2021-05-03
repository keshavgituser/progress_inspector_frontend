package com.capgemini.piapi.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;

import com.capgemini.piapi.domain.Client;
import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.ClientAlreadyExistException;
import com.capgemini.piapi.exception.ClientNotFoundException;
import com.capgemini.piapi.exception.ClientPassedNullException;
import com.capgemini.piapi.exception.DeveloperNotFoundException;
import com.capgemini.piapi.exception.LoginException;
import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.exception.TaskNotFoundException;
import com.capgemini.piapi.repository.ClientRepository;
import com.capgemini.piapi.repository.ProductOwnerRepository;
import com.capgemini.piapi.repository.RemarkRepository;
import com.capgemini.piapi.repository.TaskRepository;

/**
 * @author Keshav
 *
 */
class ClientServiceImplTest {
	@Mock
	MockHttpSession session;
	
	@InjectMocks
	ClientServiceImpl clientServiceImpl;
	
	private ProductOwner productOwner1;
	private ProductOwner productOwner2;
	private ProductOwner productOwner3;
	private List<ProductOwner> productOwnerList;
	
	@Mock
	ProductOwnerRepository productOwnerRepository;

	@Mock
	ClientRepository clientRepository;

	@Mock
	RemarkRepository remarkRepository;
	
	@Mock
	TaskRepository taskRepository;
	
	private Task task1;
	private Task task2;
	private Client client;
	private Client client1;
	private Client client2;
	private Client client3;
	
	private List<Task> taskList;
	private List<Client> clientList;
	private Remark remark;

	@BeforeEach
	public void setup() {	
		MockitoAnnotations.openMocks(this); // invoke mocks
		taskList=new ArrayList<>();
		client = new Client();

		task1 = new Task("Test Task", "T01", "test desc1", "test",productOwner3);
		task2 = new Task("Test Task1", "T02", "test desc2", "test12",productOwner3);
		new Task("Test Task1", "T03", "test desc2", "test12",productOwner3);
		taskList.add(task1);	
		taskList.add(task2);	
		client = new Client("Test Client", "testclient", "testclient123");
		client1 = new Client("Test Client", "testclient", "testclient123");
		client2 = new Client("Test Client", "testclient", "testclient123");

		
		productOwnerList=new ArrayList<>();
				
		productOwner1=new ProductOwner("Test Owner1", "Test", "Test123");
		productOwner2=new ProductOwner("Test Owner2", "Test1", "Test1234");
		productOwner3=new ProductOwner("Test Owner3", "Test2", "Test123");
		
		productOwnerList.add(productOwner1);
		productOwnerList.add(productOwner2);
		productOwnerList.add(productOwner3);		
		
		new ProductOwner();
		
		clientList = new ArrayList<>();
		clientList.add(client);
		clientList.add(client1);
		clientList.add(client2);
		
		client3 = new Client("Test Client", "testclient", "testclient123");
		client3.setTask(taskList);
		
		remark = new Remark("Test Remark","Client", task1);
		
		
	}
	//-----------------------------------------------Register Test---------------------------------------------------------------------
		// REGISTER TEST CASE  :  Successful Registration
	
	@Test
	void test_saveClient_GivenClient_ShouldReturnSavedClient() {  
		when(clientRepository.save(client1)).thenReturn(new Client("Test Client", "testclient", "testclient123"));
		Client savedClient = clientServiceImpl.saveClient(client1);
		assertEquals(client1.getClientName(), savedClient.getClientName());		
		assertEquals(client1.getLoginName(), savedClient.getLoginName());	
		assertEquals(client1.getPwd(), savedClient.getPwd());	
	}
	
	// REGISTER TEST CASE  :  Empty Client Name
	@Test
	void test_saveClient_Given_null_Client_Name_Should_ThrowClientPassedNullException() 
	{
		client.setClientName(null);
		client.setLoginName("New Loginfirst");
		client.setPwd("New passfirst");
		Exception ex=assertThrows(ClientPassedNullException.class, ()->clientServiceImpl.saveClient(client));
		assertEquals("ClientName is Null", ex.getMessage());
		
		
	}
	
	// REGISTER TEST CASE  :  Client Already Exist
		@Test
		void test_saveClient_GivenExistingClient_ShouldThrowClientAlreadyExistException() {
			when(clientRepository.findByLoginName(client1.getLoginName())).thenReturn(new Client("Test Client", "testclient", "testclient123"));
			when(clientRepository.save(client)).thenThrow(ClientAlreadyExistException.class);
			Exception ex=assertThrows(ClientAlreadyExistException.class, ()->clientServiceImpl.saveClient(client1));
			assertEquals("Client Already Exist Please Login", ex.getMessage());
		}	
	
	
	
	// REGISTER TEST CASE  :  Empty Client loginName
	@Test
	void test_saveClient_Given_null_login_Name_Should_ThrowClientPassedNullException() 
	{
		client.setClientName("New ClientName");
		client.setLoginName(null);
		client.setPwd("New passfirst");
		Exception ex=assertThrows(ClientPassedNullException.class, ()->clientServiceImpl.saveClient(client));
		assertEquals("loginName is Null", ex.getMessage());		
	}
	// REGISTER TEST CASE  :  Empty Client Password
	@Test
	void test_saveClient_Given_null_Pwd_Should_ThrowClientPassedNullException() 
	{
		client.setClientName("New ClientName");
		client.setLoginName("new LoginName");
		client.setPwd(null);
		Exception ex=assertThrows(ClientPassedNullException.class, ()->clientServiceImpl.saveClient(client));
		assertEquals("Password is Null", ex.getMessage());		
	}
	
	//FIND TEST CASE : Empty loginName
	@Test
	void test_findByLoginName_Given_String_LoginName_ThrowsClientPassedNullException()
	{
		String loginName=null;
		
		assertThrows(ClientPassedNullException.class,()-> clientServiceImpl.findByLoginName(loginName));
	}
	//FIND TEST CASE: Valid loginName
	@Test
	void test_findByLoginName_given_String_loginName_Return_Client()
	{
		when(clientRepository.findByLoginName("testclient")).thenReturn(client);
		Client foundclient=clientServiceImpl.findByLoginName("testclient");
		assertEquals(client, foundclient);	
		
	}
	//DELETE TEST CASE: GIVEN loginName
	@Test
	void test_deleteClientByLoginName_Given_String_loginName_Removes_Client()
	{
		when(clientRepository.findByLoginName("testclient")).thenReturn(client);
		clientServiceImpl.deleteClientByLoginName("testclient");
		when(clientRepository.findByLoginName("testclient")).thenReturn(null);
		assertEquals(null,clientServiceImpl.findByLoginName("testclient")); 
		
	}
	//DELETE TEST CASE :GIVEN INVALID LOGIN NAME
	@Test
	void test_deleteClientByLoginName_Given_String_loginName_Throw_ClientNotFoundException()
	{
		when(clientRepository.findByLoginName("testclient")).thenReturn(null);
		Exception ex=assertThrows(ClientNotFoundException.class,()->clientServiceImpl.deleteClientByLoginName("testclient"));
		assertEquals("Client with loginName : testclient does not exists", ex.getMessage());		
	}
	
	//-----------------------------------------------Login Test-------------------------------------------------------------------------
		// LOGIN TEST CASE  :  Successful Login
	@Test
	void test_authenticateClient_Given_String_loginName_String_Password_Return_User()
	{
		when(clientRepository.save(client1)).thenReturn(new Client("Test Client", "testclient", "testclient123"));
		clientServiceImpl.saveClient(client1);
		when(clientRepository.findByLoginName(client1.getLoginName())).thenReturn(client1);
		when(session.getAttribute("loginName")).thenReturn(client1.getLoginName());
		Client client= clientServiceImpl.authenticateClient(client1.getLoginName(),client1.getPwd(), session);
		assertNotNull(client);
		assertNotNull(client.getLoginName());
		assertEquals(client.getLoginName(),session.getAttribute("loginName"));	
	}
	// LOGIN TEST CASE : Wrong Login Name
	@Test
	void test_authenticateClient_Given_String_loginName_String_Password_Return_ClientNotFoundException()
	{

		Exception ex=assertThrows(ClientNotFoundException.class, ()->clientServiceImpl.authenticateClient("Test2", "Test2", session));	
		assertEquals("Client with loginName : Test2 does not exist", ex.getMessage());
	}
	// LOGIN TEST CASE : Null Values
	@Test
	void test_authenticateClient_Given_null_loginName_null_Password_Return_ClientNotFoundException()
	{

		Exception ex=assertThrows(LoginException.class, ()->clientServiceImpl.authenticateClient(null, null, session));	
		assertEquals("Please Enter Credentials",ex.getMessage());
	}
	//FIND TEST CASE: NOT ENTERED ANY CLIENTS
	@Test
	void test_findAllClients_ThrowsClientNotFoundException_If_ClientsList_IsEmpty()
	{
		//when(clientRepository.findAll()).thenReturn(null);
		Exception ex=assertThrows(ClientNotFoundException.class,()->clientServiceImpl.getAllClients());
		assertEquals("Clients Not Found", ex.getMessage());
	}
	
	//-----------------------------------------------Update Client Test----------------------------------------------------------
	
		
	
	//UPDATE TEST CASE:INVALID 
	@Test
	void test_updateClient_null_Client_Throw_ClientNotFoundException()
	{
		when(clientRepository.findByLoginName(client1.getLoginName())).thenReturn(null);
		Exception ex=assertThrows(ClientNotFoundException.class,()-> clientServiceImpl.updateClient(client1));
		assertEquals("Client with loginName : testclient does not exists", ex.getMessage());
	}
	
	// UPDATE PRODUCTOWNER TEST : update the Client
	@Test
	void test_updateClient_Client_Returns_Updated_Client()
	{
		
		when(clientRepository.save(client2)).thenReturn(new Client("Test Client", "testclient", "testclient123"));
		when(clientRepository.findByLoginName(client2.getLoginName())).thenReturn(new Client("Old Test Client", "testclient", "testclient123"));
		assertEquals(client2.getClientName(), clientServiceImpl.updateClient(client2).getClientName());
		
	}
	
	//-----------------------------------------------Remarks Client Test----------------------------------------------------------
	//Test to add remark to the specific task
		@Test
		void test_addReamrk_GivenTaskIdentifierRemarkAndTaskIdentifier_ShouldReturnAddedRemark() {
			BDDMockito.given(taskRepository.findByTaskIdentifier(task1.getTaskIdentifier()))
					.willReturn(task1);
			BDDMockito.given(taskRepository.save(task1))
					.willReturn(task1);
			BDDMockito.given(remarkRepository.save(remark))
					.willReturn(remark);
			Remark savedRemark = clientServiceImpl.addRemark(remark, task1.getTaskIdentifier());
			assertEquals(remark.getDescription(), savedRemark.getDescription());
		}
		
		// Test to add remark to the specific task with Invalid task identifier
		// TaskIdException is thrown
		@Test
		void test_addReamrk_GivenNullValues_ShouldThrowNullPointerException() {
			BDDMockito.given(taskRepository.findByTaskIdentifier(task1.getTaskIdentifier()))
					.willReturn(null);
			assertThrows(TaskIdException.class, () -> clientServiceImpl.addRemark(remark, "T01"));
		}
		
		// Test to add remark to the specific task with Empty task identifier 
		// TaskIdException is thrown
		@Test
		void test_addReamrk_GivenWrongTaskIdentifier_ShouldThrowTaskIdException() {
			BDDMockito.given(taskRepository.findByTaskIdentifier(""))
					.willReturn(null);
			assertThrows(TaskIdException.class, () -> clientServiceImpl.addRemark(remark, ""));
		}

	//View: Return valid Task 
	@Test
	public void test_viewTask_GivenClientLoginNameAndTaskIdentifier_ShouldReturnTask() {
		when(clientRepository.findByLoginName(client3.getLoginName()))
		.thenReturn(client3);
		Task returnedTask = clientServiceImpl.viewTask(client3.getLoginName(), "T01");
		assertEquals("T01", returnedTask.getTaskIdentifier());

	}
	
	@Test
	public void test_viewTask_GivenInvalidClientLoginNameAndTaskIdentifier_ShouldThrowClientNotFoundException() {
		when(clientRepository.findByLoginName(client3.getLoginName()))
		.thenReturn(null);
//		Task returnedTask = clientServiceImpl.viewTask(client3.getLoginName(), "T01");
//		assertEquals("T01", returnedTask.getTaskIdentifier());
		assertThrows(ClientNotFoundException.class, () -> clientServiceImpl.viewTask(client3.getLoginName(), "T01"));
	}
	@Test
	public void test_viewTask_GivenClientLoginNameAndInvalidTaskIdentifier_ShouldThrowTaskIdException() {
		when(clientRepository.findByLoginName(client3.getLoginName()))
		.thenReturn(client3);
//		Task returnedTask = clientServiceImpl.viewTask(client3.getLoginName(), "T01");
//		assertEquals("T01", returnedTask.getTaskIdentifier());
		assertThrows(TaskIdException.class, () -> clientServiceImpl.viewTask(client3.getLoginName(), "T03"));
	}
	@Test
	public void test_viewTask_GivenClientLoginNameAndEmptyTaskIdentifier_ShouldThrowClientNotFoundException() {
		when(clientRepository.findByLoginName(client3.getLoginName()))
		.thenReturn(client3);
//		Task returnedTask = clientServiceImpl.viewTask(client3.getLoginName(), "T01");
//		assertEquals("T01", returnedTask.getTaskIdentifier());
		assertThrows(TaskIdException.class, () -> clientServiceImpl.viewTask(client3.getLoginName(), ""));
	}
	//View task:null Task identifier
	@Test
	public void test_viewTask_GivenEmptyClientLoginNameAndTaskIdentifier_ShouldThrowClientNotFoundException() {
		when(clientRepository.findByLoginName(""))
		.thenReturn(null);
		assertThrows(ClientNotFoundException.class, () -> clientServiceImpl.viewTask("","T01"));
	}
	
	//-----------------------------------------------View All Tasks Test----------------------------------------------------------
		// VIEW TASK PROGRESS : Successful
		@Test
		void test_viewAllTasks_ShouldReturnListOfTasks() {
			when(session.getAttribute("loginName")).thenReturn(client3.getLoginName());
			when(clientRepository.findByLoginName(session.getAttribute("loginName").toString()))
			.thenReturn(client3);
			List<Task> tasks = clientServiceImpl.viewAllTask(session);
			assertNotNull(tasks);
			assertEquals(2, tasks.size());
			assertEquals(task1.getTaskIdentifier(), tasks.get(0).getTaskIdentifier());
			assertEquals(task2.getTaskIdentifier(), tasks.get(1).getTaskIdentifier());

		}

		// VIEW TASK PROGRESS : Task Not found Exception
		@Test
		void test_getAllTasks_ShouldThrowTaskNotFoundException() {
			when(session.getAttribute("loginName")).thenReturn(client2.getLoginName());
			when(clientRepository.findByLoginName(client2.getLoginName()))
					.thenReturn(client2);
			when(session.getAttribute("loginName")).thenReturn(client2.getLoginName());
			Exception ex = assertThrows(TaskIdException.class, () -> clientServiceImpl.viewAllTask(session));
			assertEquals("No task with task id found", ex.getMessage());
		}
	
	
	
	
	@AfterEach
	public void cleanUp()
	{
		client=null;
		client=client1=client2=null;
		clientList=null;
	}


}

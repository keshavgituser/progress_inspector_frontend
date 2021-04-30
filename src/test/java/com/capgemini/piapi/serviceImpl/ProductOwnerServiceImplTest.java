package com.capgemini.piapi.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

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
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.ClientNotFoundException;
import com.capgemini.piapi.exception.LoginException;
import com.capgemini.piapi.exception.ProductOwnerAlreadyExistException;
import com.capgemini.piapi.exception.ProductOwnerNotFoundException;
import com.capgemini.piapi.exception.TaskNotFoundException;
import com.capgemini.piapi.repository.ClientRepository;
import com.capgemini.piapi.repository.ProductOwnerRepository;
import com.capgemini.piapi.repository.TaskRepository;

class ProductOwnerServiceImplTest {
	//Mock Beans
	@Mock
	MockHttpSession session;
	
	@InjectMocks
	ProductOwnerServiceImpl productOwnerServiceImpl;
	
	//Stubs 
	private ProductOwner productOwner;
	private ProductOwner productOwner1;
	private ProductOwner productOwner2;
	private ProductOwner productOwner3;
	private List<ProductOwner> productOwnerList;
	
	@Mock
	ProductOwnerRepository productOwnerRepository;

	@Mock
	ClientRepository clientRepository;
	@Mock
	TaskRepository taskRepository;
	
	private Task task1;
	private Task task2;
	private Client client;
	private List<Task> taskList;

	@BeforeEach
	public void setup() {	
		MockitoAnnotations.openMocks(this); // invoke mocks
		taskList=new ArrayList<>();
		client = new Client();

		task1 = new Task("Test Task", "t01", "test desc1", "test",productOwner3);
		task2 = new Task("Test Task1", "t02", "test desc2", "test12",productOwner3);
		taskList.add(task1);	
		taskList.add(task2);	
		client = new Client("Test Client", "testclient", "testclient123");

		productOwnerList=new ArrayList<>();
				
		productOwner1=new ProductOwner("Test Owner1", "Test", "Test123");
		productOwner2=new ProductOwner("Test Owner2", "Test1", "Test1234");
		productOwner3=new ProductOwner("Test Owner3", "Test2", "Test123");
		
		productOwnerList.add(productOwner1);
		productOwnerList.add(productOwner2);
		productOwnerList.add(productOwner3);		
		
		productOwner=new ProductOwner();

	}
	//-----------------------------------------------Register Test---------------------------------------------------------------------
	// REGISTER TEST CASE  :  Successful Registration
	@Test
	void test_saveProductOwner_GivenProductOwner_ShouldReturnSavedProductOwner() {
		BDDMockito.given(productOwnerRepository.save(productOwner1)).willReturn(new ProductOwner("Test Owner1", "Test", "Test123"));
		ProductOwner savedProductOwner = productOwnerServiceImpl.saveProductOwner(productOwner1);
		assertEquals(productOwner1.getName(), savedProductOwner.getName());		
		assertEquals(productOwner1.getLoginName(), savedProductOwner.getLoginName());	
		assertEquals(productOwner1.getPwd(), savedProductOwner.getPwd());	
	}	
	
	// REGISTER TEST CASE  :  User Already Exist
	@Test
	void test_saveProductOwner_GivenExistingProductOwner_ShouldThrowProductOwnerAlreadyExistException() {
		BDDMockito.given(productOwnerRepository.findByLoginName(productOwner1.getLoginName())).willReturn(new ProductOwner("Test Owner1", "Test", "Test123"));
		BDDMockito.given(productOwnerRepository.save(productOwner)).willThrow(ProductOwnerAlreadyExistException.class);
		Exception ex=assertThrows(ProductOwnerAlreadyExistException.class, ()->productOwnerServiceImpl.saveProductOwner(productOwner1));
		assertEquals("Product owner already exists", ex.getMessage());
	}	
	
	// REGISTER TEST CASE  :  Empty Fields
	@Test
	void test_saveProductOwner_GivenNull_ShouldThrowNullPointerExceptionException() {
		Exception ex=assertThrows(NullPointerException.class, ()->productOwnerServiceImpl.saveProductOwner(productOwner));
		assertEquals("Please Fill the Required Fields", ex.getMessage());
	
	}	
	//-----------------------------------------------Login Test-------------------------------------------------------------------------
	// LOGIN TEST CASE  :  Successful Login
	@Test
	void test_authenticateProductOwner_GivenProductOwner_ShouldReturnLoggedInProductOwner() {
		BDDMockito.given(productOwnerRepository.findByLoginName(productOwner1.getLoginName())).willReturn(new ProductOwner("Test Owner1", "Test", "Test123"));
		BDDMockito.given(session.getAttribute("loginName")).willReturn(productOwner1.getLoginName());
		ProductOwner owner= productOwnerServiceImpl.authenticateProductOwner(productOwner1.getLoginName(),productOwner1.getPwd(), session);
		assertNotNull(owner);
		assertNotNull(owner.getLoginName());
		assertEquals(owner.getLoginName(),session.getAttribute("loginName"));
	}
	
	// LOGIN TEST CASE : Wrong Login Name
	@Test
	void test_authenticateProductOwner_GivenWrongLoginName_ShouldThrowProductOwnerNotFoundException() {
		BDDMockito.given(productOwnerRepository.findByLoginName(productOwner1.getLoginName())).willThrow(ProductOwnerNotFoundException.class);
		BDDMockito.given(session.getAttribute("loginName")).willReturn("Test");
		Exception ex=assertThrows(ProductOwnerNotFoundException.class,() -> productOwnerServiceImpl.authenticateProductOwner(productOwner2.getLoginName(),productOwner1.getPwd(), session),"Product Owner with loginName : " + productOwner2.getLoginName() + " does not exist");
		assertEquals("Product Owner with loginName : " + productOwner2.getLoginName() + " does not exist", ex.getMessage());
	}
	// LOGIN TEST CASE : Wrong Password

	@Test
	void test_authenticateProductOwner_GivenWrongPassword_ShouldThrowLoginException() {
		BDDMockito.given(productOwnerRepository.findByLoginName(productOwner1.getLoginName())).willReturn(new ProductOwner("Test Owner1", "Test", "Test123"));
		BDDMockito.given(session.getAttribute("loginName")).willReturn(productOwner1.getLoginName());
		Exception ex=assertThrows(LoginException.class,() -> productOwnerServiceImpl.authenticateProductOwner(productOwner1.getLoginName(), productOwner2.getPwd(), session));
		assertEquals("Login Failed ! Invalid Credentials", ex.getMessage());
	}
	
	// LOGIN TEST CASE : Null 

		@Test
		void test_authenticateProductOwner_GivenNull_ShouldThrowLoginException() {
			BDDMockito.given(session.getAttribute("loginName")).willReturn("Test");
			Exception ex=assertThrows(LoginException.class,() -> productOwnerServiceImpl.authenticateProductOwner(productOwner.getLoginName(), productOwner.getPwd(), session));
			assertEquals("Please Enter Credentials", ex.getMessage());
		}
		//-----------------------------------------------View Task Progress Test----------------------------------------------------------
		//-----------------------------------------------Get All Tasks Test----------------------------------------------------------
		//-----------------------------------------------Add Client To Task Test----------------------------------------------------------
		// CLIENT ADD TEST CASE : Add task to client Successful
		@Test
		void test_addTaskToClient_GivenClientLoginNameAndTaskIdentifier_ShouldReturnUpdatedClient() {			
			BDDMockito.given(clientRepository.findByLoginName("testclient")).willReturn(new Client("Test Client", "testclient", "testclient123"));			
			BDDMockito.given(taskRepository.findByTaskIdentifier("taskIdentifier")).willReturn(new Task("Test Task", "t01", "test desc", "test",productOwner3));
			
			productOwner3.setTask(taskList);
			
			Client updatedClient = productOwnerServiceImpl.addTaskToClient("testclient", "taskIdentifier");			
			assertEquals(1, updatedClient.getTask().size());
			assertEquals(task1.getTaskIdentifier(), updatedClient.getTask().get(0).getTaskIdentifier());
		}
		//ClientLoginName not found 
		@Test
		void test_addTaskToClient_GivenWrongClientLoginName_ShouldThrowClientNotFoundException() {
			BDDMockito.given(clientRepository.findByLoginName(client.getLoginName())).willReturn(null);	
			BDDMockito.given(taskRepository.findByTaskIdentifier(task1.getTaskIdentifier())).willReturn(new Task("Test Task","t01","test desc","test",productOwner3));
		
			Exception ex=assertThrows(ClientNotFoundException.class,() -> productOwnerServiceImpl.addTaskToClient(client.getLoginName(),task1.getTaskIdentifier()));
			assertEquals("Client with loginName : "+client.getLoginName()+" does not exists", ex.getMessage());
		}
		//Task Identifier not found 
		@Test
		void test_addTaskToClient_GivenWrongTaskIdentifier_ShouldThrowTaskNotFoundException() {
			BDDMockito.given(clientRepository.findByLoginName(client.getLoginName())).willReturn(new Client("Test Client", "testclient", "testclient123"));
			BDDMockito.given(taskRepository.findByTaskIdentifier(task1.getTaskIdentifier())).willReturn(null);
		
			Exception ex=assertThrows(TaskNotFoundException.class,() -> productOwnerServiceImpl.addTaskToClient(client.getLoginName(),task1.getTaskIdentifier()));
			assertEquals("Task with id : "+task1.getTaskIdentifier()+" does not exists", ex.getMessage());
		}
		
		//Task Identifier  NUll
		@Test
		void test_addTaskToClient_GivenNullClientLoginName_ShouldThrowNullPointerException() {
			Exception ex=assertThrows(NullPointerException.class,() -> productOwnerServiceImpl.addTaskToClient(client.getLoginName(),null));
			assertEquals("Please Provide Required Fields", ex.getMessage());
		}
		//ClientLoginName  NUll
		@Test
		void test_addTaskToClient_GivenNullTaskIdentifier_ShouldThrowNullPointerException() {
			Exception ex=assertThrows(NullPointerException.class,() -> productOwnerServiceImpl.addTaskToClient(null,task1.getTaskIdentifier()));
			assertEquals("Please Provide Required Fields", ex.getMessage());
		}
		//-----------------------------------------------Get All Clients Test----------------------------------------------------------
		//-----------------------------------------------Update Product Owner Test----------------------------------------------------------
		//-----------------------------------------------Delete Product Owner Test----------------------------------------------------------
		//----------------------------------------------------------------------------------------------------------------------------------
	@AfterEach
	public void cleanUp()
	{
		productOwner=null;
		productOwner1=productOwner2=productOwner3=null;
		productOwnerList=null;
	}

}

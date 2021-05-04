package com.capgemini.piapi.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
	private Task task3;
	private Client client;
	private Client client1;
	private Client client2;
	private List<Task> taskList;
	private List<Client> clientList;

	/**
	 * Dummy objects for testing
	 */
	@BeforeEach
	public void setup() {	
		MockitoAnnotations.openMocks(this); // invoke mocks
		taskList=new ArrayList<>();
		client = new Client();

		task1 = new Task("Test Task", "T01", "test desc1", "test",productOwner3);
		task2 = new Task("Test Task1", "T02", "test desc2", "test12",productOwner3);
		task3 = new Task("Test Task1", "T03", "test desc2", "test12",productOwner3);
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
		
		productOwner=new ProductOwner();
		
		clientList = new ArrayList<>();
		clientList.add(client);
		clientList.add(client1);
		clientList.add(client2);
		

	}
	//-----------------------------------------------Register Test---------------------------------------------------------------------
	// REGISTER TEST CASE  :  Successful Registration
	@Test
	void test_saveProductOwner_GivenProductOwner_ShouldReturnSavedProductOwner() {  
		when(productOwnerRepository.save(productOwner1)).thenReturn(new ProductOwner("Test Owner1", "Test", "Test123"));
		ProductOwner savedProductOwner = productOwnerServiceImpl.saveProductOwner(productOwner1);
		assertEquals(productOwner1.getName(), savedProductOwner.getName());		
		assertEquals(productOwner1.getLoginName(), savedProductOwner.getLoginName());	
		assertEquals(productOwner1.getPwd(), savedProductOwner.getPwd());	
	}	
	
	// REGISTER TEST CASE  :  User Already Exist
	@Test
	void test_saveProductOwner_GivenExistingProductOwner_ShouldThrowProductOwnerAlreadyExistException() {
		when(productOwnerRepository.findByLoginName(productOwner1.getLoginName())).thenReturn(new ProductOwner("Test Owner1", "Test", "Test123"));
		when(productOwnerRepository.save(productOwner)).thenThrow(ProductOwnerAlreadyExistException.class);
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
		when(productOwnerRepository.findByLoginName(productOwner1.getLoginName())).thenReturn(new ProductOwner("Test Owner1", "Test", "Test123"));
		when(session.getAttribute("loginName")).thenReturn(productOwner1.getLoginName());
		ProductOwner owner= productOwnerServiceImpl.authenticateProductOwner(productOwner1.getLoginName(),productOwner1.getPwd(), session);
		assertNotNull(owner);
		assertNotNull(owner.getLoginName());
		assertEquals(owner.getLoginName(),session.getAttribute("loginName"));
	}
	
	// LOGIN TEST CASE : Wrong Login Name
	@Test
	void test_authenticateProductOwner_GivenWrongLoginName_ShouldThrowProductOwnerNotFoundException() {
		when(productOwnerRepository.findByLoginName(productOwner1.getLoginName())).thenThrow(ProductOwnerNotFoundException.class);
		when(session.getAttribute("loginName")).thenReturn("Test");
		Exception ex=assertThrows(ProductOwnerNotFoundException.class,() -> productOwnerServiceImpl.authenticateProductOwner(productOwner2.getLoginName(),productOwner1.getPwd(), session),"Product Owner with loginName : " + productOwner2.getLoginName() + " does not exist");
		assertEquals("Product Owner with loginName : " + productOwner2.getLoginName() + " does not exist", ex.getMessage());
	}
	// LOGIN TEST CASE : Wrong Password

	@Test
	void test_authenticateProductOwner_GivenWrongPassword_ShouldThrowLoginException() {
		when(productOwnerRepository.findByLoginName(productOwner1.getLoginName())).thenReturn(new ProductOwner("Test Owner1", "Test", "Test123"));
		when(session.getAttribute("loginName")).thenReturn(productOwner1.getLoginName());
		Exception ex=assertThrows(LoginException.class,() -> productOwnerServiceImpl.authenticateProductOwner(productOwner1.getLoginName(), productOwner2.getPwd(), session));
		assertEquals("Invalid Credentials", ex.getMessage());

	}
	
	// LOGIN TEST CASE : Null 

	@Test
	void test_authenticateProductOwner_GivenNull_ShouldThrowLoginException() {
		when(session.getAttribute("loginName")).thenReturn("Test");
		Exception ex = assertThrows(LoginException.class, () -> productOwnerServiceImpl
				.authenticateProductOwner(productOwner.getLoginName(), productOwner.getPwd(), session));
		assertEquals("Please Enter Credentials", ex.getMessage());
	}
		
	//-----------------------------------------------View All Tasks Test----------------------------------------------------------
	// VIEW TASK PROGRESS : Successful
	@Test
	void test_getAllTasks_ShouldReturnListOfTask() {
		productOwner1.setTask(taskList);
		when(productOwnerRepository.findByLoginName(productOwner1.getLoginName()))
				.thenReturn(productOwner1);
		when(session.getAttribute("loginName")).thenReturn("Test");
		List<Task> tasks = productOwnerServiceImpl.getAllTasks(session);
		assertNotNull(tasks);
		assertEquals(2, tasks.size());
		assertEquals(task1.getTaskIdentifier(), tasks.get(0).getTaskIdentifier());
		assertEquals(task2.getTaskIdentifier(), tasks.get(1).getTaskIdentifier());

	}

	// VIEW TASK PROGRESS : Task Not found Exception
	@Test
	void test_getAllTasks_ShouldThrowTaskNotFoundException() {
		when(productOwnerRepository.findByLoginName(productOwner1.getLoginName()))
				.thenReturn(productOwner1);
		when(session.getAttribute("loginName")).thenReturn(productOwner1.getLoginName());
		Exception ex = assertThrows(TaskNotFoundException.class, () -> productOwnerServiceImpl.getAllTasks(session));
		assertEquals("Tasks not available", ex.getMessage());
	}

	//-----------------------------------------------View Task Progress Test----------------------------------------------------------
	// VIEW TASK PROGRESS : Task Found
	@Test
	void test_getTaskByTaskIdentifier_GivenTaskIdentifier_ShouldReturnTask() {
		productOwner1.setTask(taskList);
		when(productOwnerRepository.findByLoginName(productOwner1.getLoginName()))
				.thenReturn(productOwner1);
		when(session.getAttribute("loginName")).thenReturn("Test");
		Task task = productOwnerServiceImpl.getTaskByTaskIdentifier(task1.getTaskIdentifier(), session);
		assertNotNull(task);
		assertEquals(task1.getTaskIdentifier(), task.getTaskIdentifier());
		assertEquals(task1.getTitle(), task.getTitle());
		assertEquals(task1.getDescription(), task.getDescription());
		assertEquals(task1.getProgress(), task.getProgress());

	}

	// VIEW TASK PROGRESS : Task Not found Exception
	@Test
	void test_getTaskByTaskIdentifier_GivenWrongTaskIdentifier_ShouldThrowTaskNotFoundException() {
		productOwner1.setTask(taskList);
		when(productOwnerRepository.findByLoginName(productOwner1.getLoginName()))
				.thenReturn(productOwner1);
		when(session.getAttribute("loginName")).thenReturn(productOwner1.getLoginName());
		Exception ex = assertThrows(TaskNotFoundException.class,
				() -> productOwnerServiceImpl.getTaskByTaskIdentifier(task3.getTaskIdentifier(), session));
		assertEquals("Task with id : '" + task3.getTaskIdentifier() + "' does not exists", ex.getMessage());
	}

	// VIEW TASK PROGRESS : Task ID Null
	@Test
	void test_getTaskByTaskIdentifier_GivenNullTaskIdentifier_ShouldThrowNullPointerException() {
		productOwner1.setTask(taskList);
		when(productOwnerRepository.findByLoginName(productOwner1.getLoginName()))
				.thenReturn(productOwner1);
		when(session.getAttribute("loginName")).thenReturn(productOwner1.getLoginName());
		Exception ex = assertThrows(NullPointerException.class,
				() -> productOwnerServiceImpl.getTaskByTaskIdentifier(null, session));
		assertEquals("Please Provide Task Identifier", ex.getMessage());
	}

	//-----------------------------------------------Add Client To Task Test----------------------------------------------------------
	// CLIENT ADD TEST CASE : Add task to client Successful
	@Test
	void test_addTaskToClient_GivenClientLoginNameAndTaskIdentifier_ShouldReturnUpdatedClient() {
		when(clientRepository.findByLoginName("testclient"))
				.thenReturn(new Client("Test Client", "testclient", "testclient123"));
		when(taskRepository.findByTaskIdentifier("taskIdentifier"))
				.thenReturn(new Task("Test Task", "T01", "test desc", "test", productOwner3));

		productOwner3.setTask(taskList);

		Client updatedClient = productOwnerServiceImpl.addTaskToClient("testclient", "taskIdentifier");
		assertEquals(1, updatedClient.getTask().size());
		assertEquals(task1.getTaskIdentifier(), updatedClient.getTask().get(0).getTaskIdentifier());
	}

	//  CLIENT ADD TEST CASE : ClientLoginName not found
	@Test
	void test_addTaskToClient_GivenWrongClientLoginName_ShouldThrowClientNotFoundException() {
		when(clientRepository.findByLoginName(client.getLoginName())).thenReturn(null);
		when(taskRepository.findByTaskIdentifier(task1.getTaskIdentifier()))
				.thenReturn(new Task("Test Task", "t01", "test desc", "test", productOwner3));

		Exception ex = assertThrows(ClientNotFoundException.class,
				() -> productOwnerServiceImpl.addTaskToClient(client.getLoginName(), task1.getTaskIdentifier()));
		assertEquals("Client with loginName : " + client.getLoginName() + " does not exists", ex.getMessage());
	}

	//  CLIENT ADD TEST CASE : Task Identifier not found
	@Test
	void test_addTaskToClient_GivenWrongTaskIdentifier_ShouldThrowTaskNotFoundException() {
		when(clientRepository.findByLoginName(client.getLoginName()))
				.thenReturn(new Client("Test Client", "testclient", "testclient123"));
		when(taskRepository.findByTaskIdentifier(task1.getTaskIdentifier())).thenReturn(null);

		Exception ex = assertThrows(TaskNotFoundException.class,
				() -> productOwnerServiceImpl.addTaskToClient(client.getLoginName(), task1.getTaskIdentifier()));
		assertEquals("Task with id : " + task1.getTaskIdentifier() + " does not exists", ex.getMessage());
	}

	//  CLIENT ADD TEST CASE : Task Identifier NUll
	@Test
	void test_addTaskToClient_GivenNullClientLoginName_ShouldThrowNullPointerException() {
		Exception ex = assertThrows(NullPointerException.class,
				() -> productOwnerServiceImpl.addTaskToClient(client.getLoginName(), null));
		assertEquals("Please Provide Required Fields", ex.getMessage());
	}

	//   CLIENT ADD TEST CASE : ClientLoginName NUll
	@Test
	void test_addTaskToClient_GivenNullTaskIdentifier_ShouldThrowNullPointerException() {
		Exception ex = assertThrows(NullPointerException.class,
				() -> productOwnerServiceImpl.addTaskToClient(null, task1.getTaskIdentifier()));
		assertEquals("Please Provide Required Fields", ex.getMessage());
	}
	//-----------------------------------------------Get All Clients Test----------------------------------------------------------
	// GET ALL CLIENTS TEST CASE : get all clients
	@Test
	void test_getAllClients_ShouldReturnClientList() {
		when(clientRepository.findAll()).thenReturn(clientList);
		assertEquals(3, productOwnerServiceImpl.getAllClients().size());

	}

	// GET ALL CLIENTS TEST CASE : Clients not found
	@Test
	void test_getAllClients_ShouldThrowClientNotFoundException() {
		when(clientRepository.findAll()).thenThrow(ClientNotFoundException.class);
		Exception ex = assertThrows(ClientNotFoundException.class, () -> productOwnerServiceImpl.getAllClients());
		assertEquals("Clients not available", ex.getMessage());
	}
		
	//-----------------------------------------------Update Product Owner Test----------------------------------------------------------
		
	// UPDATE PRODUCTOWNER TEST : update the product owner
	@Test
	void test_updateProductOwner_GivenProductOwnerObject_ShouldReturnUpdatedProductOwner() {
		when(productOwnerRepository.save(productOwner2))
				.thenReturn(new ProductOwner("Test Owner2", "Test1", "Test1234"));
		when(productOwnerRepository.findByLoginName(productOwner2.getLoginName()))
				.thenReturn(new ProductOwner("Old Test Owner", "Test1", "Test1234"));
		assertEquals(productOwner2.getName(), productOwnerServiceImpl.updateProductOwner(productOwner2).getName());
	}

	// UPDATE PRODUCTOWNER TEST : not found
	@Test
	void test_updateProductOwner_GivenWrongProductOwnerObject_ShouldThrowProductOwnerNotFoundException() {
		when(productOwnerRepository.findByLoginName(productOwner2.getLoginName())).thenReturn(null);
		Exception ex = assertThrows(ProductOwnerNotFoundException.class,
				() -> productOwnerServiceImpl.updateProductOwner(productOwner2));
		assertEquals("Product Owner with loginName : " + productOwner2.getLoginName() + " does not exists",
				ex.getMessage());
	}

	// UPDATE PRODUCTOWNER TEST : null
	@Test
	void test_updateProductOwner_GivenNullProductOwnerObject_ShouldThrowNullPointerException() {
		Exception ex = assertThrows(NullPointerException.class,
				() -> productOwnerServiceImpl.updateProductOwner(new ProductOwner()));
		assertEquals("Please Fill the Required Fields", ex.getMessage());
	}

	//-----------------------------------------------Delete Product Owner Test----------------------------------------------------------
	// DELETE PRODUCTOWNER TEST : Successful Delete
	@Test
	void test_deleteProductOwnerByLoginName_GivenProductOwnerLoginName_ShouldDeleteProductOwner() {
		when(productOwnerRepository.findByLoginName(productOwner2.getLoginName()))
				.thenReturn(productOwner2);
		productOwnerServiceImpl.deleteProductOwnerByLoginName(productOwner2.getLoginName());
		verify(productOwnerRepository,times(1)).delete(productOwner2);

	}

	// DELETE PRODUCTOWNER TEST : Product Owner not found
	@Test
	void test_deleteProductOwnerByLoginName_GivenWrongProductOwnerLoginName_ShouldThrowProductOwnerNotFoundException() {
		when(productOwnerRepository.findByLoginName(productOwner2.getLoginName())).thenReturn(null);
		Exception ex = assertThrows(ProductOwnerNotFoundException.class,
				() -> productOwnerServiceImpl.deleteProductOwnerByLoginName(productOwner2.getLoginName()));
		assertEquals("Product Owner with loginName : " + productOwner2.getLoginName() + " does not exists",
				ex.getMessage());

	}

	// DELETE PRODUCTOWNER TEST : Product Owner is null
	@Test
	void test_deleteProductOwnerByLoginName_GivenNullProductOwnerObject_ShouldThrowNullPointerException() {
		Exception ex = assertThrows(NullPointerException.class,
				() -> productOwnerServiceImpl.deleteProductOwnerByLoginName(null));
		assertEquals("Please Provide Login Name", ex.getMessage());
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	@AfterEach
	public void cleanUp()
	{
		productOwner=null;
		productOwner1=productOwner2=productOwner3=null;
		productOwnerList=null;
	}

}

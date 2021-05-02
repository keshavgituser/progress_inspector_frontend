package com.capgemini.piapi.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;

import com.capgemini.piapi.domain.Developer;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.DeveloperAlreadyExistException;
import com.capgemini.piapi.exception.DeveloperNotFoundException;
import com.capgemini.piapi.exception.InvalidLoginException;
import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.repository.DeveloperRepository;
import com.capgemini.piapi.repository.RemarkRepository;
import com.capgemini.piapi.repository.TaskRepository;
import com.capgemini.piapi.serviceImpl.DeveloperServiceImpl;

class DevelperServiceImplTest {

	@Mock
	MockHttpSession session;

	@Mock
	DeveloperRepository developerRepository;

	@InjectMocks
	DeveloperServiceImpl developerServiceImpl;
	
	@Mock
	TaskRepository taskRepository;
	
	@Mock
	RemarkRepository remarkRepository;

	private Developer developer;
	private Developer developer1;
	private Developer developer2;
	private Developer nullDeveloper;
	private String developerLoginName;
	private String dummyLoginName;
	private String nullString;
	private String taskIdentifier;
	private String dummyTaskIdentifier;
	private Remark remark;
	private Task task1;
	private Task task2;
	private List<Task> taskList;
	private List<Remark> remarkList;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		taskList = new ArrayList<>();
		remarkList = new ArrayList<>();
		developer = new Developer("Dummy", "dummy123", "d123", "Inactive", taskList);
		developer1 = new Developer();
		nullDeveloper = null;
		dummyLoginName = "xyz";
		developerLoginName = developer.getLoginName();
		task1 = new Task("Test Task", "T01", "test desc1", "pending", developer);
		task1.setDeveloper(developer);
		task2 = new Task("Test Task1", "T02", "test desc2", "inactive", developer);
		taskIdentifier = task1.getTaskIdentifier();
		taskList.add(task1);
		taskList.add(task2);
		dummyTaskIdentifier = "ZZ01";
		developer2 = new Developer("Dummy", "dummy123", "d123", "Inactive", taskList);
		task2.setDeveloper(developer2);
		remark = new Remark("This is testing remark" , "givenBy dummy123",task2);
		remark.setTask(task2);
		remarkList.add(remark);
		task2.setRemark(remarkList);
	}

	// ---------------------------Developer---------------------------------
	// Test to save developer
	@Test
	void test_saveDeveloper_GivenDeveloper_ShouldReturnSavedDeveloper() {
		BDDMockito.given(developerRepository.save(developer))
				.willReturn(new Developer("Dummy", "dummy123", "d123", "Inactive", taskList));
		Developer savedDeveloper = developerServiceImpl.saveDeveloper(developer);
		assertEquals(savedDeveloper.getName(), developer.getName());
		assertEquals(savedDeveloper.getLoginName(), developer.getLoginName());
		assertEquals(savedDeveloper.getPwd(), developer.getPwd());
		assertEquals(savedDeveloper.getStatus(), developer.getStatus());
		assertEquals(savedDeveloper.getTasks(), developer.getTasks());
	}

	// Test for developer already exist exception
	@Test
	void test_saveDeveloper_GivenAlreadyExistingDeveloper_ShouldThrowDeveloperAlreadyExistException() {
		BDDMockito.given(developerRepository.findByLoginName(developer.getLoginName()))
				.willReturn(new Developer("Dummy", "dummy123", "d123", "Inactive", taskList));
		Exception ex = assertThrows(DeveloperAlreadyExistException.class,
				() -> developerServiceImpl.saveDeveloper(developer));
		assertEquals("developer with " + developer.getLoginName() + " login name is already available",
				ex.getMessage());
	}

	// Test if any required field is null should throw null pointer exception
	@Test
	void test_saveDeveloper_GivenNullValues_ShouldThrowNullPointerException() {
		BDDMockito.given(developerRepository.save(nullDeveloper)).willThrow(new NullPointerException());
		assertThrows(NullPointerException.class, () -> developerServiceImpl.saveDeveloper(nullDeveloper));
	}

	// ---------------------------------------Find Developer By LoginName--------------------------------
	// Test to find developer by login name
	@Test
	void test_findDeveloperByLoginName_GivenDeveloperLoginName_ShouldReturnDeveloper() {
		BDDMockito.given(developerRepository.findByLoginName(developerLoginName))
				.willReturn(new Developer("Dummy", "dummy123", "d123", "Inactive", taskList));
		Developer foundedDeveloper = developerServiceImpl.findDeveloperByDeveloperLoginName(developerLoginName);
		assertEquals(foundedDeveloper.getLoginName(), developerLoginName);
	}

	// Test to find developer by login name if not found then
	// DeveloperNotFoundException to be thrown
	@Test
	void test_findDeveloperByLoginName_GivenWrongDeveloperLoginName_ShouldThrowDeveloperNotFoundException() {
		BDDMockito.given(developerRepository.findByLoginName(dummyLoginName))
				.willThrow(new DeveloperNotFoundException());
		assertThrows(DeveloperNotFoundException.class,
				() -> developerServiceImpl.findDeveloperByDeveloperLoginName(developerLoginName));
	}

	// Test to find developer by login name if login name is null then throw
	// nullPointerException
	@Test
	void test_findDeveloperByLoginName_GivenNullDeveloperLoginName_ShouldThrowNullPointerException() {
		Exception ex = assertThrows(NullPointerException.class,
				() -> developerServiceImpl.updateDeveloper(new Developer()));
		assertEquals("Please Fill the Required Fields", ex.getMessage());
	}

	// --------------------------------Delete Developer By LoginName-----------------------------------------
	// Test to delete developer by DeveloperLoginName
	@Test
	void test_deleteDeveloperByLoginName_GivenDeveloperLoginName_ShouldDeleteDeveloper() {
		when(developerRepository.findByLoginName(developerLoginName)).thenReturn(developer);
		developerServiceImpl.deleteDeveloperbyDeveloperLoginName(developerLoginName);
		verify(developerRepository, times(1)).delete(developer);
	}

	// Test to delete developer by devloperLoginName if not found then
	// DeveloperNotFoundExcpetion to be thrown
	@Test
	void test_deleteDeveloperByLoginName_GivenWrongDeveloperLoginName_ShouldReturnDeveloperNotFoundException() {
		when(developerRepository.findByLoginName(dummyLoginName)).thenReturn(developer);
		developerServiceImpl.deleteDeveloperbyDeveloperLoginName(dummyLoginName);
		assertThrows(DeveloperNotFoundException.class,
				() -> developerServiceImpl.findDeveloperByDeveloperLoginName(developerLoginName));
	}

	// Test to delete developer by login name if login name is null then throw
	// nullPointerException
	@Test
	void test_deleteDeveloperByLoginName_GivenNullDeveloperObject_ShouldThrowNullPointerException() {
		Exception ex = assertThrows(NullPointerException.class,
				() -> developerServiceImpl.deleteDeveloperbyDeveloperLoginName(null));
		assertEquals("Please Fill the Required Fields", ex.getMessage());
	}

	// -------------------------------Update Developer By LoginName----------------------------------
	// Test Case to update developer should return updated developer
	@Test
	void test_updateDeveloper_GivenDeveloper_ShouldReturnUpdatedDeveloper() {
		when(developerRepository.save(developer))
				.thenReturn(new Developer("Dummy", "dummy123", "d123", "Inactive", taskList));
		when(developerRepository.findByLoginName(developer.getLoginName()))
				.thenReturn(new Developer("Old Test Owner", "Test1", "Test1234", "Inactive", taskList));
		assertEquals(developer.getName(), developerServiceImpl.updateDeveloper(developer).getName());
	}

	// Test Case to update developer should if developer object not found then throw
	// DeveloperNotFoundException
	@Test
	void test_updateDeveloper_GivenWrongDeveloperObject_ShouldThrowDeveloperNotFoundException() {
		when(developerRepository.findByLoginName(developer.getLoginName())).thenReturn(null);
		Exception ex = assertThrows(DeveloperNotFoundException.class,
				() -> developerServiceImpl.updateDeveloper(developer));
		assertEquals("Developer with loginName : " + developer.getLoginName() + " does not exists",
				ex.getMessage());
	}

	// Test Case to update developer should if developer is null then throw
	// NullPointerException
	@Test
	void test_updateDeveloper_GivenNullDeveloperObject_ShouldThrowNullPointerException() {
		Exception ex = assertThrows(NullPointerException.class,
				() -> developerServiceImpl.updateDeveloper(new Developer()));
		assertEquals("Please Fill the Required Fields", ex.getMessage());
	}

	// ------------------------------View All Task By Developer LoginName------------------------
	// Test to get list of task by given developer login name
	@Test
	void test_viewAllTaskByDeveloperLoginName_GivenDeveloperLoginName_ShouldReturnListOfTasks() {
		BDDMockito.given(developerRepository.findByLoginName(developerLoginName)).willReturn(developer);
		List<Task> taskList = developerServiceImpl.viewAllTaskByDeveloperLoginName(developerLoginName);
		assertEquals(taskList, developer.getTasks());
	}
	
	// Test to get list of task by given developer login if null should throw NullPointerException
	@Test
	void test_viewAllTaskByDeveloperLoginName_GivenNullDeveloperLoginName_ShouldThrowNullPointerException() {
		BDDMockito.given(developerRepository.findByLoginName(nullString)).willReturn(null);
		Exception ex = assertThrows(NullPointerException.class,
				() -> developerServiceImpl.viewAllTaskByDeveloperLoginName(nullString));
		assertEquals("Please Fill the Required Fields",
				ex.getMessage());
	}
	
	//----------------------------View Task by Identifier and Developer Login Name----------------------
	// Test to find task on the basis of Task identifier and developer login name
	@Test
	void test_findTaskByTaskIdentifierAndDevelpoerLoginName_GivenTaskIdentifierAndDeveloperLoginName_ShouldReturnTask() {
		BDDMockito.given(developerRepository.findByLoginName(developerLoginName)).willReturn(developer);
		Task task = developerServiceImpl.findTaskByTaskIdentifierAndDevelpoerLoginName(taskIdentifier,
				developerLoginName);
		assertEquals(task1.getTaskIdentifier(), task.getTaskIdentifier());
	}
	
	// Test to find task on the basis of Task identifier and developer login name if
	// any field is null then throw null pointer exception
	@Test
	void test_findTaskByTaskIdentifierAndDevelpoerLoginName_GivenNullValues_ShouldThrowNullPointerException() {
		BDDMockito.given(developerRepository.findByLoginName(nullString)).willReturn(developer);
		Exception ex = assertThrows(NullPointerException.class,
				() -> developerServiceImpl.findTaskByTaskIdentifierAndDevelpoerLoginName(taskIdentifier, nullString));
		assertEquals("Please Fill the Required Fields", ex.getMessage());
	}
	
	//	Test to find task on the basis of Task identifier and developer login name if
	// taskIdentifier is not available then TaskIdFoundException is thrown
	@Test
	void test_test_findTaskByTaskIdentifierAndDevelpoerLoginName_GivenWrongTaskIdentifier_ShouldThrowTaskIdException() {
		BDDMockito.given(developerRepository.findByLoginName(developerLoginName)).willReturn(developer);
		Exception ex = assertThrows(TaskIdException.class,
				() -> developerServiceImpl.findTaskByTaskIdentifierAndDevelpoerLoginName(dummyTaskIdentifier, developerLoginName));
		assertEquals("Task with id " + dummyTaskIdentifier.toUpperCase() + " is not available", ex.getMessage());
	}
	
	// Test to find task on the basis of Task identifier and developer login name if
	// developerloginName is not available then TaskIdFoundException is thrown
	@Test
	void test_findTaskByTaskIdentifierAndDevelpoerLoginName_GivenWrongDeveloperLoginName_ShouldThrowDeveloperNotFoundException() {
		BDDMockito.given(developerRepository.findByLoginName(dummyLoginName)).willReturn(null);
		Exception ex = assertThrows(DeveloperNotFoundException.class, () -> developerServiceImpl
				.findTaskByTaskIdentifierAndDevelpoerLoginName(taskIdentifier, dummyLoginName));
		assertEquals("Developer with loginName : " + dummyLoginName + " does not exists", ex.getMessage());
	}
	
	//------------------------------------Update Task Status---------------------------------------------
	// Test to update task status on the basis taskIdentifier , developerloginName ,
	// task
	@Test
	void test_updateTaskStatus_GivenTaskIdentifierDeveloperLoginNameandTask_ShouldReturnUpdatedTask() {
		BDDMockito.given(developerRepository.findByLoginName(developer2.getLoginName()))
				.willReturn(developer2);
		BDDMockito.given(taskRepository.save(task2))
				.willReturn(new Task("Test Task1", "T02", "test desc2", "testing", developer2));
		Task task = developerServiceImpl.updateTaskStatus("T02", developer2.getLoginName(),
				new Task("Test Task1", "T02", "test desc2", "testing", developer2));
		assertEquals("testing", task.getProgress());
	}
	
	//Test to update update task status on the basis of taskIdentifier , developerloginName ,Task 
	//If null values occured then it should 
	@Test
	void test_updateTaskStatus_GivenNullValues_ShouldThorwNullPointerException() {
		BDDMockito.given(developerRepository.findByLoginName(developer2.getLoginName())).willReturn(developer2);
		BDDMockito.given(taskRepository.save(task2))
				.willReturn(new Task("Test Task1", "T02", "test desc2", "testing", developer2));
		Exception ex = assertThrows(NullPointerException.class, () -> developerServiceImpl.updateTaskStatus("T02",
				nullString, new Task("Test Task1", "T02", "test desc2", "testing", developer2)));
		assertEquals("Please Fill the Required Fields", ex.getMessage());
	}
	
	//Test to update update task status on the basis of taskIdentifier , developerloginName ,Task
	//If task not found should throw TaskIdException
	@Test
	void test_updateTaskStatus_GivenWrongTaskIdentifier_ShouldThrowTaskIdException() {
		BDDMockito.given(developerRepository.findByLoginName(developer2.getLoginName())).willReturn(developer2);
		BDDMockito.given(taskRepository.save(task2))
				.willReturn(new Task("Test Task1", "T02", "test desc2", "testing", developer2));
		Exception ex = assertThrows(TaskIdException.class, () -> developerServiceImpl.updateTaskStatus(dummyTaskIdentifier,
				developer2.getLoginName(), new Task("Test Task1", "T02", "test desc2", "testing", developer2)));
		assertEquals("Task with Identifer" + dummyTaskIdentifier.toUpperCase() + " doesn't exist", ex.getMessage());
	}
	
	// Test to update update task status on the basis of taskIdentifier ,
	// developerloginName ,Task
	// If developer not found should throw DeveloperNotFound Exception
	@Test
	void test_updateTaskStatus_GivenWrongDeveloperLoginName_ShouldThrowDeveloperNotFound() {
		BDDMockito.given(developerRepository.findByLoginName(developer2.getLoginName())).willReturn(developer2);
		BDDMockito.given(taskRepository.save(task2))
				.willReturn(new Task("Test Task1", "T02", "test desc2", "testing", developer2));
		Exception ex = assertThrows(DeveloperNotFoundException.class,
				() -> developerServiceImpl.updateTaskStatus("T02", dummyLoginName,
						new Task("Test Task1", "T02", "test desc2", "testing", developer2)));
		assertEquals("developer with " + dummyLoginName + " does not exist", ex.getMessage());
	}
	
	//----------------------------------------Add Remark------------------------------------------------
	//Test to add remark to the specific task
	@Test
	void test_addReamrk_GivenTaskIdentifierDeveloperLoginNameAndRemark_ShouldReturnAddedRemark() {
		BDDMockito.given(developerRepository.findByLoginName(developer2.getLoginName())).willReturn(developer2);
		BDDMockito.given(taskRepository.save(task2))
				.willReturn(new Task("Test Task1", "T02", "test desc2", "testing", developer2 , remarkList));
		BDDMockito.given(remarkRepository.save(remark))
				.willReturn(new Remark("This is testing remark", "givenBy dummy123", task2));
		Task task = developerServiceImpl.addRemark(remark.getTask().getTaskIdentifier(), developer2.getLoginName(),
				new Remark("This is testing remark", "givenBy dummy123", task2));
		assertEquals("This is testing remark", remark.getDescription());
	}
	
	// Test to add remark to the specific task if null values found
	// NullPointerException is thrown
	@Test
	void test_addReamrk_GivenNullValues_ShouldThrowNullPointerException() {
		BDDMockito.given(developerRepository.findByLoginName(developer2.getLoginName())).willReturn(developer2);
		BDDMockito.given(taskRepository.save(task2))
				.willReturn(new Task("Test Task1", "T02", "test desc2", "testing", developer2, remarkList));
		BDDMockito.given(remarkRepository.save(remark))
				.willReturn(new Remark("This is testing remark", "givenBy dummy123", task2));
		Exception ex = assertThrows(NullPointerException.class,
				() -> developerServiceImpl.addRemark(remark.getTask().getTaskIdentifier(), nullString,
						new Remark("This is testing remark", "givenBy dummy123", task2)));
		assertEquals("Please Fill the Required Fields", ex.getMessage());
	}
	
	// Test to add remark to the specific task if task id not found then
	// TaskIdException is thrown
	@Test
	void test_addReamrk_GivenWrongTaskIdentifier_ShouldThrowTaskIdException() {
		BDDMockito.given(developerRepository.findByLoginName(developer2.getLoginName())).willReturn(developer2);
		BDDMockito.given(taskRepository.save(task2))
				.willReturn(new Task("Test Task1", "T02", "test desc2", "testing", developer2, remarkList));
		BDDMockito.given(remarkRepository.save(remark))
				.willReturn(new Remark("This is testing remark", "givenBy dummy123", task2));
		Exception ex = assertThrows(TaskIdException.class, () -> developerServiceImpl.addRemark(dummyTaskIdentifier,
				developer2.getLoginName(), new Remark("This is testing remark", "givenBy dummy123", task2)));
		assertEquals("Task with Identifer" + dummyTaskIdentifier.toUpperCase() + " doesn't exist", ex.getMessage());
	}
	
		// Test to add remark to the specific task if developer not found exception
		// should be thrown
	@Test
	void test_addReamrk_GivenWrongDeveloperLoginName_ShouldThrowDeveloperNotFound() {
		BDDMockito.given(developerRepository.findByLoginName(developer2.getLoginName())).willReturn(developer2);
		BDDMockito.given(taskRepository.save(task2))
				.willReturn(new Task("Test Task1", "T02", "test desc2", "testing", developer2, remarkList));
		BDDMockito.given(remarkRepository.save(remark))
				.willReturn(new Remark("This is testing remark", "givenBy dummy123", task2));
		Exception ex = assertThrows(DeveloperNotFoundException.class, () -> developerServiceImpl.addRemark(remark.getTask().getTaskIdentifier(),
				dummyLoginName, new Remark("This is testing remark", "givenBy dummy123", task2)));
		assertEquals("developer with " + dummyLoginName + " does not exist", ex.getMessage());
	}
	
	//--------------------------------------------Login Authentication Developer----------------------------------------------
	// Test case to successfully login Developer
	@Test
	void test_authenticateDeveloper_GivenProductOwner_ShouldReturnLoggedInDeveloper() {
		when(developerRepository.findByLoginName(developer.getLoginName()))
				.thenReturn(new Developer("Dummy", "dummy123", "d123", "Inactive", taskList));
		when(session.getAttribute("developerLoginName")).thenReturn(developer.getLoginName());
		Developer dev = developerServiceImpl.authenticateDeveloper(developer.getLoginName(), developer.getPwd(),
				session);
		assertNotNull(dev);
		assertNotNull(dev.getLoginName());
		assertEquals(dev.getLoginName(), session.getAttribute("developerLoginName"));
	}
	
	// Test case to successfully login Developer if wrong login name is entered
	// DeveloperNotFoundException is thrown
	@Test
	void test_authenticateDevloper_GivenWrongDeveloperLoginName_ShouldReturnDeveloperNotFoundException() {
		when(developerRepository.findByLoginNameAndPwd(developer.getLoginName(), developer.getPwd()))
				.thenReturn(new Developer("Dummy", "dummy123", "d123", "Inactive", taskList));
		when(session.getAttribute("developerLoginName")).thenReturn(developer.getLoginName());
		Exception ex = assertThrows(DeveloperNotFoundException.class,
				() -> developerServiceImpl.authenticateDeveloper(developer.getLoginName(), developer.getPwd(), session),
				"Developer with loginName " + developer.getLoginName() + " does not exist");
		assertEquals("Developer with loginName : " + developer.getLoginName() + " does not exist", ex.getMessage());
	}
	
	// Test case to successfully login Developer if wrong password is
	// entered invalid login exception is thrown
	@Test
	void test_authenticateDevloper_GivenWrongPassword_ShouldReturnInvalidLoginException() {
		when(developerRepository.findByLoginName(developer.getLoginName()))
				.thenReturn(new Developer("Dummy", "dummy123", "xxx", "Inactive", taskList));
		when(session.getAttribute("developerLoginName")).thenReturn(developer.getLoginName());
		Exception ex = assertThrows(InvalidLoginException.class, () -> developerServiceImpl
				.authenticateDeveloper(developer.getLoginName(), developer.getPwd(), session));
		assertEquals("Login Failed ! Invalid Credentials", ex.getMessage());
	}
	
	// Test case to authenticate login if given null values enter
	// InvalidLoginException
	@Test
	void test_authenticateDeveloper_GivenNull_ShouldThrowInvalidLoginException() {
		when(session.getAttribute("developerLoginName")).thenReturn(developer1);
		Exception ex = assertThrows(InvalidLoginException.class, () -> developerServiceImpl
				.authenticateDeveloper(developer1.getLoginName(), developer1.getPwd(), session));
		assertEquals("Please Enter Credentials", ex.getMessage());
	}
}
	
	

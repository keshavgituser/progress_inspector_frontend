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
	
	
}
	
	

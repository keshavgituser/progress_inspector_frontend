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
	

}
	
	

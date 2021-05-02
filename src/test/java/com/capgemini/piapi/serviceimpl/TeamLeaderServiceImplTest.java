package com.capgemini.piapi.serviceimpl;

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

import com.capgemini.piapi.domain.Client;
import com.capgemini.piapi.domain.Developer;
import com.capgemini.piapi.domain.ProductOwner;
import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.domain.TeamLeader;
import com.capgemini.piapi.exception.DeveloperNotFoundException;
import com.capgemini.piapi.exception.InvalidLoginException;
import com.capgemini.piapi.exception.ProductOwnerNotFoundException;
import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.exception.TaskNotFoundException;
import com.capgemini.piapi.exception.TeamLeaderAlreadyExistsException;
import com.capgemini.piapi.exception.TeamLeaderNotFoundException;
import com.capgemini.piapi.repository.ClientRepository;
import com.capgemini.piapi.repository.DeveloperRepository;
import com.capgemini.piapi.repository.ProductOwnerRepository;
import com.capgemini.piapi.repository.TaskRepository;
import com.capgemini.piapi.repository.TeamLeaderRepository;
import com.capgemini.piapi.serviceImpl.TeamLeaderServiceImpl;

class TeamLeaderServiceImplTest {

	@InjectMocks
	TeamLeaderServiceImpl teamLeaderServiceImpl;

	@Mock
	TeamLeaderRepository teamLeaderRepository;

	@Mock
	TaskRepository taskRepository;

	@Mock
	DeveloperRepository developerRepository;

	@Mock
	ProductOwnerRepository productOwnerRepository;

	@Mock
	ClientRepository clientRepository;

	@Mock
	MockHttpSession session;

	// Stub

	String teamLeaderLoginName;
	String productOwnerLoginName;
	String developerLoginName;
	String taskId;

	private Task task;
	private Task task1;
	private List<Task> tasks;

	private TeamLeader teamLeader;
	private TeamLeader teamLeader1;
	private TeamLeader teamLeaderTest;
	private List<TeamLeader> teamLeaders;

	private Developer developer;
	private Developer developer1;
	private Developer developerList;

	private ProductOwner productOwner;
	private ProductOwner productOwner1;
	private List<ProductOwner> productOwnerList;

	private Client client;
	private Client client1;
	private List<Client> clients;

	private Remark remark1;
	private Remark remark2;
	private List<Remark> remarkList;

	@BeforeEach
	void setUp() throws Exception {

		MockitoAnnotations.openMocks(this);// invoke mocks
		tasks = new ArrayList<>();
		task = new Task("Test Task", "t01", "test desc1", "test", productOwner, teamLeader, developer);
		taskId = task.getTaskIdentifier().toUpperCase();
		tasks.add(task);
		task1 = new Task();
		task1.setTitle("testTitle");
		task1.setTaskIdentifier("t01");
		task1.setRemark(remarkList);

		teamLeader = new TeamLeader("Test TeamLeader", "Test", "Test123", tasks);
		teamLeaderTest = new TeamLeader();
		teamLeaderTest.setLoginName("test1");
		teamLeaderTest.setName("test1");
		teamLeaderTest.setPwd("test1");
		teamLeaderLoginName = teamLeader.getLoginName();

		productOwner = new ProductOwner("Test owner", "Test", "Test123", client, tasks);
		productOwnerLoginName = productOwner.getLoginName();
		developer = new Developer("Test Developer", "Test", "Test123", "Inactive", tasks);
		developerLoginName = developer.getLoginName();

		remark1 = new Remark();
		remark1.setDescription("testRemark");
		remark1.setGivenBy("test");

		remark2 = new Remark();
		remark2.setDescription("testRemark2");
		remark2.setGivenBy("test2");
		remarkList = new ArrayList<Remark>();
		remarkList.add(remark1);
		remarkList.add(remark2);

	}

	// Test:Registration of TeamLeader
	// Given TeamLeader will be saved And return the registered TeamLeader
	@Test
	void test_RegisterTeamLeader_GivenTeamLeader_ShouldReturnSavedTeamLeader() {
		TeamLeader teamLeader = new TeamLeader();
		teamLeader.setName("Test");
		teamLeader.setLoginName("testuser");
		teamLeader.setPwd("test123");

		when(teamLeaderRepository.save(teamLeader)).thenReturn(teamLeader);

		TeamLeader savedTeamLeader = teamLeaderServiceImpl.registerTeamLeader(teamLeader);

		assertEquals(teamLeader.getName(), savedTeamLeader.getName());
		assertEquals(teamLeader.getLoginName(), savedTeamLeader.getLoginName());
		assertEquals(teamLeader.getPwd(), savedTeamLeader.getPwd());

	}

	// Test:Registration of TeamLeader
	// Given try to register the Already Existing TeamLeader will Throw
	// TeamLeaderAlreadyExistsException

	@Test
	void test_RegisterTeamLeader_GivenExistingTeamLeader_ShouldThrowTeamLeaderAlreadyExistsException() {
		TeamLeader teamLeader = new TeamLeader();
		teamLeader.setName("Test");
		teamLeader.setLoginName("testuser");
		teamLeader.setPwd("test123");

		when(teamLeaderRepository.findByLoginName(teamLeader.getLoginName())).thenReturn(teamLeader);

		assertThrows(TeamLeaderAlreadyExistsException.class,
				() -> teamLeaderServiceImpl.registerTeamLeader(teamLeader));

	}

	// Test: FindingTeamLeaderByLoginName

	@Test
	void test_FindTeamLeaderByLoginName_GivenLoginName_ShouldReturnTeamLeader() {

		TeamLeader teamLeader = new TeamLeader();
		teamLeader.setLoginName("testuser");
		teamLeader.setName("Test");
		teamLeader.setPwd("test123");

		when(teamLeaderRepository.findByLoginName("testuser")).thenReturn(teamLeader);

		TeamLeader fetchedTeamLeader = teamLeaderServiceImpl.findTeamLeaderByLoginName(teamLeader.getLoginName());

		assertEquals(teamLeader.getName(), fetchedTeamLeader.getName());
		assertEquals(teamLeader.getLoginName(), fetchedTeamLeader.getLoginName());
		assertEquals(teamLeader.getPwd(), fetchedTeamLeader.getPwd());

	}

	// Test : FindingTeamLeaderByLoginName
	// If no TeamLeader found Throw TeamLeaderNotFoundExcedption
	@Test
	void test_FindTeamLeaderByLoginName_GivenLoginName_ShouldThrowTeamLeaderNotFoundException() {
		TeamLeader teamLeader = new TeamLeader();
		teamLeader.setLoginName("testusernotavailable");
		teamLeader.setName("Test");
		teamLeader.setPwd("test123");

		when(teamLeaderRepository.findByLoginName(teamLeader.getLoginName())).thenReturn(null);

		assertThrows(TeamLeaderNotFoundException.class,
				() -> teamLeaderServiceImpl.findTeamLeaderByLoginName(teamLeader.getLoginName()));
	}

	// Test : updateTeamLeader
	// Given TeamLeader Should Throw TeamLeaderNotFoundException
	@Test
	void test_UpdateTeamLeader_Given_TeamLeader_Should_Throw_TeamLeaderNotFoundException() {
		TeamLeader teamLeader = new TeamLeader();
		teamLeader.setLoginName("testuser");
		teamLeader.setName("Test");
		teamLeader.setPwd("test123");

		when(teamLeaderRepository.findByLoginName(teamLeader.getLoginName())).thenReturn(null);

		assertThrows(TeamLeaderNotFoundException.class, () -> teamLeaderServiceImpl.updateTeamLeader(teamLeader));
	}

	// Test : updateTeamLeader
	// if no LoginName Then throw null pointerException
	@Test
	void test_UpdateTeamLeader_Given_NullValues_ShouldThrowNullPointerExceptionException() {
		TeamLeader teamLeader = new TeamLeader();
		teamLeader.setName("Test");
		teamLeader.setPwd("test123");

		when(teamLeaderRepository.findByLoginName(teamLeader.getLoginName())).thenReturn(null);

		assertThrows(NullPointerException.class, () -> teamLeaderServiceImpl.updateTeamLeader(teamLeader));
	}

	// Test: updateTeamLeader
	// update the given TeamLeader
	@Test
	void test_UpdateTeamLeader_GivenTeamLeader_ShouldReturnUpdatedTeamLeader() {

		TeamLeader updatedTeamLeader = new TeamLeader();
		updatedTeamLeader.setLoginName("test1");
		updatedTeamLeader.setName("UpdatedTest");
		updatedTeamLeader.setPwd("updatedtest123");

		when(teamLeaderRepository.findByLoginName(updatedTeamLeader.getLoginName())).thenReturn(teamLeaderTest);

		when(teamLeaderRepository.save(updatedTeamLeader)).thenReturn(updatedTeamLeader);

		TeamLeader getTeamLeader = teamLeaderServiceImpl.updateTeamLeader(updatedTeamLeader);
		assertEquals(updatedTeamLeader.getName(), getTeamLeader.getName());
	}

	// Test :Delete TeamLeader
	// teamLeader with loginName will be deleted
	@Test
	void test_deleteTeamLeaderByLoginName_GivenTeamLeaderLoginName_ShouldDeleteTeamLeader() {
		when(teamLeaderRepository.findByLoginName(teamLeaderTest.getLoginName())).thenReturn(teamLeaderTest);
		teamLeaderServiceImpl.deleteTeamLeaderByLoginName(teamLeaderTest.getLoginName());
		verify(teamLeaderRepository, times(1)).deleteByLoginName(teamLeaderTest.getLoginName());

	}

	// Test : Delete TeamLeader
	// if cannot find TeamLeader with given loginName then throw
	// TeamLeaderNotFoundException
	@Test
	void test_deleteTeamLeaderByLoginName_GivenTeamLeaderLoginName_ShouldThrowTeamLeaderNotFoundException() {
		when(teamLeaderRepository.findByLoginName(teamLeaderTest.getLoginName())).thenReturn(null);

		assertThrows(TeamLeaderNotFoundException.class,
				() -> teamLeaderServiceImpl.deleteTeamLeaderByLoginName(teamLeaderTest.getLoginName()));

	}

	// Test : Authenticate TeamLeader
	// if credentials are valid then return loggedIn TeamLeader
	@Test
	void test_authenticateTeamLeader_GivenTeamLeaderCredentials_ShouldReturnLoggedInTeamLeader() {
		when(teamLeaderRepository.findTeamLeaderByLoginNameAndPwd(teamLeaderTest.getLoginName(),
				teamLeaderTest.getPwd())).thenReturn(teamLeaderTest);
		when(session.getAttribute("loginName")).thenReturn(teamLeaderTest.getLoginName());
		TeamLeader loggedTeamLeader = teamLeaderServiceImpl.authenticateTeamLeader(teamLeaderTest.getLoginName(),
				teamLeaderTest.getPwd(), session);
		assertNotNull(loggedTeamLeader);
		assertNotNull(loggedTeamLeader.getLoginName());
		assertEquals(loggedTeamLeader.getLoginName(), session.getAttribute("loginName"));
	}

	// Test: Authenticate TeamLeader
	// if wrong credentials then throw InvalidLoginException
	@Test
	void test_authenticateTeamLeader_GivenWrongTeamLeaderCredentials_ShouldThrowInvalidLoginException() {
		when(teamLeaderRepository.findTeamLeaderByLoginNameAndPwd(teamLeaderTest.getLoginName(),
				teamLeaderTest.getPwd())).thenReturn(null);
		assertThrows(InvalidLoginException.class, () -> teamLeaderServiceImpl
				.authenticateTeamLeader(teamLeaderTest.getLoginName(), teamLeaderTest.getPwd(), session));
	}

	// Test : viewAllremark
	// return the remarks in the task whose taskIdentifier is given
	@Test
	void test_viewAllRemark_GivenTaskIdentifier_ShouldReturnAllRemarksInThe_task() {
		when(taskRepository.findByTaskIdentifier(task1.getTaskIdentifier())).thenReturn(task1);
		assertEquals(task1.getRemark(), teamLeaderServiceImpl.viewAllRemark(task1.getTaskIdentifier()));

	}

	// Test : viewAllRemark
	// throw TaskNotFoundException if cannot find task by given TaskIdentifier
	@Test
	void test_viewAllRemark_GivenWrongTaskIdentifier_ShouldThrowTaskNotFoundException() {
		when(taskRepository.findByTaskIdentifier(task1.getTaskIdentifier())).thenReturn(null);
		assertThrows(TaskNotFoundException.class, () -> teamLeaderServiceImpl.viewAllRemark(task1.getTaskIdentifier()));
	}

	// Test : viewAllDeveloper
	// return the List of Developers
	@Test
	void test_viewAllDevelopers_ShouldReturnListOfDevelopers() {

		List<Developer> developerList = new ArrayList<Developer>();
		developerList.add(developer);
		developerList.add(developer1);

		when(developerRepository.findAll()).thenReturn(developerList);
		assertEquals(developerList, teamLeaderServiceImpl.findAllDevelopers());

	}

	// Test : viewAllDevelopers
	// If no developers is found then throw DeveloperNotFoundException
	@Test
	void test_viewAllDevelopers_ShouldThrowDeveloperNotFoundException() {
		List<Developer> emptyDeveloperList = new ArrayList<Developer>();
		when(developerRepository.findAll()).thenReturn(emptyDeveloperList);
		assertThrows(DeveloperNotFoundException.class, () -> teamLeaderServiceImpl.findAllDevelopers());
	}

	// ----------------------------Task Test-------------------------------
	// TASK TEST CASE: Create Task

	@Test
	void test_saveTask_GivenTaskDetails_ShouldReturnSavedTaskDetails() {
		BDDMockito.given(productOwnerRepository.findByLoginName(productOwnerLoginName)).willReturn(productOwner);
		BDDMockito.given(teamLeaderRepository.findByLoginName(teamLeaderLoginName)).willReturn(teamLeader);
		BDDMockito.given(taskRepository.save(task)).willReturn(task);
		BDDMockito.given(teamLeaderRepository.save(teamLeader)).willReturn(teamLeader);
		BDDMockito.given(productOwnerRepository.save(productOwner)).willReturn(productOwner);

		Task savedTask = teamLeaderServiceImpl.createTask(task, productOwnerLoginName, teamLeaderLoginName);
		assertEquals(savedTask.getTitle(), task.getTitle());
		assertEquals(savedTask.getTaskIdentifier(), task.getTaskIdentifier());
		assertEquals(savedTask.getDescription(), task.getDescription());
		assertEquals(savedTask.getProgress(), task.getProgress());
		assertEquals(savedTask.getProductOwner(), task.getProductOwner());
		assertEquals(savedTask.getTeamLeader(), task.getTeamLeader());
		assertEquals(savedTask.getDeveloper(), task.getDeveloper());

	}

	// TASK TEST CASE: Empty Fields for Product Owner

	@Test
	void test_saveTask_GivenWrongProductOwner_ShouldThrowProductOwnerNotFoundException() {
		BDDMockito.given(productOwnerRepository.findByLoginName("Test123")).willReturn(null);
		BDDMockito.given(teamLeaderRepository.findByLoginName(teamLeaderLoginName)).willReturn(teamLeader);
		BDDMockito.given(taskRepository.save(task)).willReturn(task);
		BDDMockito.given(teamLeaderRepository.save(teamLeader)).willReturn(teamLeader);
		BDDMockito.given(productOwnerRepository.save(productOwner)).willReturn(productOwner);

		Exception ex = assertThrows(ProductOwnerNotFoundException.class,
				() -> teamLeaderServiceImpl.createTask(task, "Test123", teamLeaderLoginName));
		assertEquals("Product Owner Not Found", ex.getMessage());

	}

	// TASK TEST CASE: Empty Fields for Team Leader

	@Test
	void test_saveTask_GivenWrongTeamLeader_ShouldThrowTeamLeaderNotFoundException() {
		BDDMockito.given(productOwnerRepository.findByLoginName("Test123")).willReturn(productOwner);
		BDDMockito.given(teamLeaderRepository.findByLoginName(teamLeaderLoginName)).willReturn(null);
		BDDMockito.given(taskRepository.save(task)).willReturn(task);
		BDDMockito.given(teamLeaderRepository.save(teamLeader)).willReturn(teamLeader);
		BDDMockito.given(productOwnerRepository.save(productOwner)).willReturn(productOwner);

		Exception ex = assertThrows(TeamLeaderNotFoundException.class,
				() -> teamLeaderServiceImpl.createTask(task1, "Test123", teamLeaderLoginName));
		assertEquals("Team Leader not found", ex.getMessage());

	}

	// TASK TEST CASE: Duplicate Task

	@Test
	void test_saveTask_GivenWrongDuplicateTask_ShouldThrowTaskIdException() {
		BDDMockito.given(productOwnerRepository.findByLoginName("Test123")).willReturn(productOwner);
		BDDMockito.given(teamLeaderRepository.findByLoginName(teamLeaderLoginName)).willReturn(teamLeader);
		BDDMockito.given(taskRepository.save(task)).willThrow(TaskIdException.class);
		BDDMockito.given(teamLeaderRepository.save(teamLeader)).willReturn(teamLeader);
		BDDMockito.given(productOwnerRepository.save(productOwner)).willReturn(productOwner);

		assertThrows(TaskIdException.class,
				() -> teamLeaderServiceImpl.createTask(task, "Test123", teamLeaderLoginName));
	}

	// ----------------------Assign Task To Developer
	// Test----------------------------
	// ASSIGN TASK TEST CASE: Assign

	@Test
	void test_assignTaskToDeveloper_GivenTaskIdentifierAndDeveloperLoginName_ShouldAssignTaskToDeveloper() {
		BDDMockito.given(developerRepository.findByLoginName(developerLoginName)).willReturn(developer);
		BDDMockito.given(taskRepository.findByTaskIdentifier(taskId)).willReturn(task);
		BDDMockito.given(taskRepository.save(task)).willReturn(task);
		BDDMockito.given(developerRepository.save(developer)).willReturn(developer);

		Task assignDeveloper = teamLeaderServiceImpl.assignDeveloper(taskId, developerLoginName);
		assertEquals(assignDeveloper.getTaskIdentifier(), task.getTaskIdentifier());
		assertEquals(assignDeveloper.getDeveloper(), task.getDeveloper());

	}

	// ASSIGN TASK TEST CASE: Empty field for Developer

	@Test
	void test_assignTaskToDeveloper_GivenWrongDeveloperLoginName_ShouldThrowDeveloperNotFoundException() {
		BDDMockito.given(developerRepository.findByLoginName("Test123")).willReturn(null);
		BDDMockito.given(taskRepository.findByTaskIdentifier(taskId)).willReturn(task);
		BDDMockito.given(taskRepository.save(task)).willReturn(task);
		BDDMockito.given(developerRepository.save(developer)).willReturn(developer);
		Exception ex = assertThrows(DeveloperNotFoundException.class,
				() -> teamLeaderServiceImpl.assignDeveloper(taskId, developerLoginName));
		assertEquals("Developer with Identifer " + developerLoginName + " doesn't exist", ex.getMessage());

	}

	// ASSIGN TASK TEST CASE: Empty field for Task

	@Test
	void test_assignTaskToDeveloper_GivenWrongTaskIdentifier_ShouldThrowTaskIdException() {
		BDDMockito.given(developerRepository.findByLoginName(developerLoginName)).willReturn(developer);
		BDDMockito.given(taskRepository.findByTaskIdentifier(taskId)).willReturn(null);
		BDDMockito.given(taskRepository.save(task)).willReturn(task);
		BDDMockito.given(developerRepository.save(developer)).willReturn(developer);
		Exception ex = assertThrows(TaskIdException.class,
				() -> teamLeaderServiceImpl.assignDeveloper(taskId, developerLoginName));
		assertEquals("Task with Identifer " + taskId.toUpperCase() + " doesn't exist", ex.getMessage());

	}

	// DELETE TASK TEST CASE: Delete Task

	@Test
	void test_deleteTaskByTaskIdentifier_GivenTaskByTaskIdentifier_ShouldDeleteTask() {
		when(taskRepository.findByTaskIdentifier(taskId)).thenReturn(task);
		teamLeaderServiceImpl.deleteTask(taskId);
		verify(taskRepository, times(1)).delete(task);

	}

	// DELETE TASK TEST CASE: Task field Empty

	@Test
	void test_deleteTaskByTaskIdentifier_GivenTaskByTaskIdentifier_ShouldThrowTaskIdException() {
		when(taskRepository.findByTaskIdentifier(taskId)).thenReturn(null);
		assertThrows(TaskIdException.class, () -> teamLeaderServiceImpl.deleteTask(taskId));

	}

	// VIEW TASK TEST CASE: View task

	@Test
	void test_viewTask_GivenTaskIdentifierAndTeamLeaderLoginName_ShouldReturnTask() {
		BDDMockito.given(teamLeaderRepository.findByLoginName(teamLeaderLoginName)).willReturn(teamLeader);

		Task savedTask = teamLeaderServiceImpl.findTaskByTaskIdentifierAndTeamLeaderLoginName("t01",
				teamLeaderLoginName);
		assertEquals(savedTask.getTitle(), task.getTitle());
		assertEquals(savedTask.getTaskIdentifier(), task.getTaskIdentifier());

	}

	/// VIEW TASK TEST CASE :For Throwing Task Exception

	@Test
	void test_viewTask_GivenWrongTaskIdentifierAndTeamLeaderLoginName_ShouldThrowException() {
		BDDMockito.given(teamLeaderRepository.findByLoginName(teamLeaderLoginName)).willReturn(teamLeader);
		Exception ex = assertThrows(TaskIdException.class,
				() -> teamLeaderServiceImpl.findTaskByTaskIdentifierAndTeamLeaderLoginName("T02", teamLeaderLoginName));
		assertEquals("Task with id T02 is not available", ex.getMessage());

	}

	/// VIEW TASK TEST CASE: View all Task

	@Test
	void test_viewTaskAll_ShouldReturnAllTask() {
		BDDMockito.given(teamLeaderRepository.findByLoginName(teamLeaderLoginName)).willReturn(teamLeader);
		List<Task> listTask = teamLeaderServiceImpl.viewAllTaskByTeamLeaderLoginName(teamLeaderLoginName);
		assertEquals(listTask.size(), 1);

	}

}

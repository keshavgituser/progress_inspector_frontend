package com.capgemini.piapi.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.capgemini.piapi.domain.Remark;
import com.capgemini.piapi.domain.Task;
import com.capgemini.piapi.exception.ClientNotFoundException;
import com.capgemini.piapi.exception.TaskIdException;
import com.capgemini.piapi.service.ClientService;

/**
 * This is the test class for the Client Service.
 * @author Hrushikesh
 *
 */

@SpringBootTest
class ClientServiceImplTest {

	@Autowired
	private ClientService clientService;
	
	
	//Test cases related to addRemark method in the client service
	//Add remark Successfully
	@Test
	public void test_addRemark_GivenRemark_ShouldReturnSavedRemark() {
		Remark remark = new Remark();
		remark.setDescription("Test Owner");
		remark.setGivenBy("Test");
		Remark savedremark = clientService.addRemark(remark, "task");
		assertEquals(remark, savedremark);	
	}
	
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
	
	
	//These are the test cases related to viewTask method in the client service.
	//View task Successful
	@Test
	public void test_viewTask_GivenClientLoginNameAndTaskIdentifier_ShouldReturnTask() {
		Task task = new Task();
		task.setDescription("task");
		task.setProgress("tsk");
		task.setTaskIdentifier("task");
		task.setTitle("task");
		Task task1 = clientService.viewTask("client", "task");
		assertEquals(task.getDescription(), task1.getDescription());	
		assertEquals(task.getProgress(), task1.getProgress());
		assertEquals(task.getTaskIdentifier(), task1.getTaskIdentifier());
		assertEquals(task.getTitle(), task1.getTitle());
	}
	
	//View task Unsuccessful because Invalid Task identifier
	@Test
	public void test_viewTask_GivenClientrLoginNameandInvalidTaskIdentifier_ShouldThrowTaskIdException() {
		assertThrows(TaskIdException.class, () -> clientService.viewTask("client", "tk"));
	}
	
	//View task Unsuccessful because Invalid Client login Name
	@Test
	public void test_viewTask_GivenInvalidClientLoginNameAndValidTaskIdentifier_ShouldThrowTaskIdException() {
		assertThrows(ClientNotFoundException.class, () -> clientService.viewTask("clint", "task"));
	}

	//View task Unsuccessful because Empty Task identifier
	@Test
	public void test_viewTask_GivenClientrLoginNameandEmptyTaskIdentifier_ShouldThrowTaskIdException() {
		assertThrows(TaskIdException.class, () -> clientService.viewTask("client", ""));
	}
	
	//View task Unsuccessful because Empty Client login Name
	@Test
	public void test_viewTask_GivenEmptyClientLoginNameAndValidTaskIdentifier_ShouldThrowTaskIdException() {
		assertThrows(ClientNotFoundException.class, () -> clientService.viewTask("", "task"));
	}
	
/*

 */
}

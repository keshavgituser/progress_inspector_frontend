package com.capgemini.piapi.constant;

import org.springframework.stereotype.Component;

/**
 * Task Constant is used to show current status of task if task is pending , in
 * progress , testing or completed as per requirements
 * 
 * @author Harshit Verma
 *
 */
@Component
public class TaskConstants {

	public static final String TASK_STATUS_PENDING = "Pending";
	public static final String TASK_STATUS_INPROGRESS = "In Progress";
	public static final String TASK_STATUS_TESTING = "Testing";
	public static final String TASK_STATUS_COMPLETED = "Completed";

}

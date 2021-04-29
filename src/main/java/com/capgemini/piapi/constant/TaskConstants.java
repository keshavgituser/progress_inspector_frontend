package com.capgemini.piapi.constant;

import org.springframework.stereotype.Component;


/**
 * Progress report to show task status
 */
@Component
public class TaskConstants {

	public static final String TASK_STATUS_PENDING = "Pending";
	public static final String TASK_STATUS_INPROGRESS = "In Progress";
	public static final String TASK_STATUS_TESTING = "Testing";
	public static final String TASK_STATUS_COMPLETED = "Completed";

}

package com.capgemini.piapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.piapi.domain.Task;
/**
 * This TaskRepository will be responsible for performing all the CRUD operations 
 * on  Task
 * @author 
 *
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	public Task findByTaskIdentifier(String taskIdentifier);

}

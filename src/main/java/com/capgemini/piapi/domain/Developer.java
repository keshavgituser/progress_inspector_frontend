package com.capgemini.piapi.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This is the main domain model for developer defining attributes
 * 
 * @author Harsh Joshi
 * @author Vatsal Shah
 * @author Harshit Verma
 *
 */
/**
 * @author Harsh Joshi
 *
 */
@Entity
@Table(name = "developers")
public class Developer {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	@NotNull(message = "Developer Name is required")
	private String name;
	@NotNull(message = "Login name can not be null")
	@Column(unique = true, updatable = false)
	private String loginName;
	@NotNull(message = "Password is required")
	@Size(min = 8, max = 20, message = "Please Enter password of Minimum 8 and Maximum 20")
	private String pwd;
	private String status;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "developer")
	private List<Task> tasks = new ArrayList<>();

	public Developer() {
		super();
	}

	// Getter and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Override
	public String toString() {
		return "Developer [id=" + id + ", name=" + name + ",  loginname=" + loginName + ", pwd="
				+ pwd + ", status=" + status + ", task=" + tasks + "]";
	}

}

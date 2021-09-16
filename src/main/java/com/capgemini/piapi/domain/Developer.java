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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * This Developer Domain is used as data transfer object between layers
 * 
 * @author Harshit Verma
 *
 */
@Entity
@Table(name = "developers")
public class Developer {

	/**
	 * Primary key of Developer
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	/**
	 * Name of developer
	 */
	@NotBlank(message = "Please Enter Name.")
	private String name;
	/**
	 * Login name of developer
	 */
	@NotBlank(message = "Please Enter Login Name.")
	@Column(unique = true, updatable = false)
	private String loginName;
	/**
	 * Password of developer
	 */
	@NotBlank(message = "Please Enter Password.")
	@Size(min = 8, max = 20, message = "Please Enter password of Minimum 8 and Maximum 20")
	private String pwd;
	/**
	 * Status of developer Active or Inactive
	 */
	private String status;
	/**
	 * Relationship of one developer to many task
	 */
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "developer")
	private List<Task> tasks = new ArrayList<>();

	public Developer() {
		super();
	}

	public Developer(String name, String loginName, String pwd, String status, List<Task> tasks) {
		super();
		this.name = name;
		this.loginName = loginName;
		this.pwd = pwd;
		this.status = status;
		this.tasks = tasks;
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
		return "Developer [id=" + id + ", name=" + name + ",  loginname=" + loginName + ", pwd=" + pwd + ", status="
				+ status + ", task=" + tasks + "]";
	}

}
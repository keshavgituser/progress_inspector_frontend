package com.capgemini.piapi.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * This ProductOwner Domain is used as data transfer object between layers
 * @author Aadesh Juvekar
 *
 */
@Entity
@Table(name = "productOwners")
public class ProductOwner {
	/**
	 * id of the ProductOwner
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * name of the ProductOwner
	 */
	@NotNull(message = "Product Owner Name is required")
	private String name;
	/**
	 * Login Name of the ProductOwner
	 */
	@NotNull(message = "User Name is required")
	@Column(unique = true, updatable = false)
	private String loginName;
	/**
	 * password of the ProductOwner
	 */
	@NotNull(message = "Password is required")
	private String pwd;
	/**
	 * List of the tasks in the sprint
	 */
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "productOwner")
	private List<Task> task;
	
	/**
	 * Default Constructor and Getter Setters 
	 */
	public ProductOwner() {
		super();
	}
	
	/**
	 * Product Owner Constructor for Registration
	 * @param name of the product owner
	 * @param loginName of the product owner
	 * @param pwd of the product owner
	 */
	public ProductOwner(String name, String loginName, String pwd) {
		this.name = name;
		this.loginName = loginName;
		this.pwd = pwd;
	}

	public ProductOwner(String name, String loginName, String pwd, List<Task> tasks) {
		super();
		this.name = name;
		this.loginName = loginName;
		this.pwd = pwd;
		this.task = tasks;

	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public List<Task> getTask() {
		return task;
	}

	public void setTask(List<Task> task) {
		this.task = task;
	}

}

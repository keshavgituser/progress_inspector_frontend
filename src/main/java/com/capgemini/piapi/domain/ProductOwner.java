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
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
/**
 * This ProductOwner Domain is used as data transfer object between layers
 * @author Aadesh Juvekar
 *
 */
@Entity
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
	 * Client of the ProductOwner
	 */
	@OneToOne
	private Client client;
//	/**
//	 * List of the tasks in the sprint
//	 */
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "productOwner")
//	private List<Task> task;

	public ProductOwner() {
		super();
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


	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

//	public List<Task> getTask() {
//		return task;
//	}
//
//	public void setTask(List<Task> task) {
//		this.task = task;
//	}

}

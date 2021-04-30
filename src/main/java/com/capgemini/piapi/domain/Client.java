package com.capgemini.piapi.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 *This class is the Domain class for the Client Entity.
 *This Client Class is used to declare all the attributes from the Client.
 *
 *@author Hrushikesh
 */

@Entity
public class Client {

	/**
	 * Id is the primary key for client entity
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * client name and it cannot be blank
	 */
	@NotBlank(message = "Please enter Name.")
	private String clientName;
	/**
	 * login name of client
	 */
	@NotBlank(message = "Please enter Login Name.")
	private String loginName;
	/**
	 * password of client
	 */
	@NotBlank(message = "Please enter Password.")
	private String pwd;
	/**
	 * Status of client
	 */
	private String status;
	/**
	 * Each client can have multiple task i.e One to Many Relationships
	 */
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "task_authorized",joinColumns =@JoinColumn(name= "id"),inverseJoinColumns = @JoinColumn(name="taskIdentifier") )
	private List<Task> task;
	
	public Client() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}

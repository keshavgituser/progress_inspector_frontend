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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * This Team Leader Domain is used as data transfer object between layers
 * @author Mantu
 *
 */
@Entity
@Table(name="teamleaders")
public class TeamLeader {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message = "Please Enter Name.")
	private String name;
	@Column(unique = true)
	@NotBlank(message = "Please Enter Login Name.")
	private String loginName;
	@NotBlank(message = "Please Enter Password.")
	private String pwd;
	@JsonIgnore
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "teamLeader",orphanRemoval = true)
	private List<Task> task;

	public TeamLeader() {
		super();
	}
	
	public TeamLeader(String name, String loginName, String pwd, List<Task> task) {
		this.name = name;
		this.loginName = loginName;
		this.pwd = pwd;
		this.task = task;
	}

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

	public List<Task> getTask() {
		return task;
	}

	public void setTask(List<Task> task) {
		this.task = task;
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

}
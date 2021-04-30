package com.capgemini.piapi.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This Task Domain is used as data transfer object between layers
 * 
 * @author Tejas Naik
 */
@Entity
@Table(name = "Tasks")
public class Task {
	/**
	 * id of the Task
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	/**
	 * Title of the task
	 */
	@NotNull(message = "Task title is required")
	private String title;
	/**
	 * Unique Identifier of the task
	 */
	@NotNull(message = "Task identifier is required")
	@Column(unique = true, updatable = false)
	@Size(min = 2, max = 4, message = "Please enter valid task identifier")
	private String taskIdentifier;
	/**
	 * Description of the task
	 */
	@NotNull(message = "Task description is required")
	private String description;
	/**
	 * Progress of the task
	 */
	@NotNull(message = "Task Progress is required")
	private String progress;
	/**
	 * Owner of the product
	 */
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "owner_id", updatable = false, nullable = false)
	private ProductOwner productOwner;
	/**
	 * TeamLeader 
	 */
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private TeamLeader teamLeader;
	/**
	 * List of remarks on the task Given By Developer as well as client
	 */
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "task")
	private List<Remark> remark = new ArrayList<>();
	/**
	 * List of authorized clients on the task Given By Product Owner
	 */
	@JsonIgnore
	@ManyToMany
	private List<Client> client=new ArrayList<>();
	/**
	 * List of developers on the task Given By Team Leader
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	private Developer developer;
	/**
	 * Creation Date of the task
	 */
	@JsonFormat(pattern = "dd/mm/yyyy")
	private Date createdAt;
	/**
	 * Last Update done on the task
	 */
	@JsonFormat(pattern = "dd/mm/yyyy")
	private Date updatedAt;


	public Task() {
		super();
	}


	public Task(String title, String taskIdentifier, String description, String progress, ProductOwner productOwner) {
		this.title = title;
		this.taskIdentifier = taskIdentifier;
		this.description = description;
		this.progress = progress;
		this.productOwner=productOwner;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTaskIdentifier() {
		return taskIdentifier;
	}

	public void setTaskIdentifier(String taskIdentifier) {
		this.taskIdentifier = taskIdentifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public ProductOwner getProductOwner() {
		return productOwner;
	}

	public void setProductOwner(ProductOwner productOwner) {
		this.productOwner = productOwner;
	}

	public List<Remark> getRemark() {
		return remark;
	}

	public void setRemark(List<Remark> remark) {
		this.remark = remark;
	}
	

	public TeamLeader getTeamLeader() {
		return teamLeader;
	}


	public void setTeamLeader(TeamLeader teamLeader) {
		this.teamLeader = teamLeader;
	}


	public List<Client> getClient() {
		return client;
	}


	public void setClient(List<Client> client) {
		this.client = client;
	}


	public Developer getDeveloper() {
		return developer;
	}


	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}


	@PrePersist()
	protected void onCreate() {
		this.createdAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}
}

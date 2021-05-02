package com.capgemini.piapi.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This Remark Domain is used as data transfer object between layers
 * 
 * @author Harsh Joshi
 *
 */
@Entity
public class Remark {
	/**
	 * Primary key for remark entity
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/**
	 * Description given as a remark it cannot be blank
	 */
	@NotBlank(message = "Description is Required")
	private String description;
	/**
	 * who is giving this remark and it cannot be blank
	 */
	@NotBlank(message = "Name is Required")
	private String givenBy;
	/**
	 * Many remarks are assigned to the task
	 */
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "task_id", updatable = false, nullable = false)
	@JsonIgnore
	private Task task;


	@JsonFormat(pattern = "yyyy-MM-dd")
  private Date createdAt;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date updatedAt;

	public Remark(String description, String givenBy, Task task) {
		super();
		this.description = description;
		this.givenBy = givenBy;
		this.task = task;
	}

	public Remark() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGivenBy() {
		return givenBy;
	}

	public void setGivenBy(String givenBy) {
		this.givenBy = givenBy;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
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

	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
	}

	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}

}
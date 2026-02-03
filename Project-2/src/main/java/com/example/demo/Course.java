package com.example.demo;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "courses")
public class Course {
      
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "teacher_id")
	private Teachers teacher;
	
	Course(){}

	protected Course(Long id, String title, String description, Teachers teacher) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.teacher = teacher;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Teachers getTeacher() {
		return teacher;
	}

	public void setTeacher(Teachers teacher) {
		this.teacher = teacher;
	}
	
	
	
}

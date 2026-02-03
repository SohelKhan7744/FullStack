package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "enrollment",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"student_id","course_id"})})
public class Enrollment {
                   
	     @Id
	     @GeneratedValue(strategy = GenerationType.IDENTITY)
	     private Long id;
	     
	     
	     @ManyToOne(optional = false)
	     @JoinColumn(name = "student_id", nullable = false)
	     private Students student;

	     
	     @ManyToOne(optional = false)
	     @JoinColumn(name = "course_id", nullable = false)
	     private Course course;
	     
	     Enrollment(){}

		 protected Enrollment(Long id, Students student, Course course) {
			super();
			this.id = id;
			this.student = student;
			this.course = course;
		 }

		 public Long getId() {
			 return id;
		 }

		 public void setId(Long id) {
			 this.id = id;
		 }

		 public Students getStudent() {
			 return student;
		 }

		 public void setStudent(Students student) {
			 this.student = student;
		 }

		 public Course getCourse() {
			 return course;
		 }

		 public void setCourse(Course course) {
			 this.course = course;
		 }
	     
	     
	     
	             
	             
}

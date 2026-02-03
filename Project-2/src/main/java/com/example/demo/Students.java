package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Students {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private UserSelf user;

    private int age;
    private String rollNumber;
    private String department;
    
    Students(){
    	
    }

 Students(Long id, String username, UserSelf user, int age, String rollNumber, String password,
			String department) {
		super();
		this.id = id;
		this.user = user;
		this.age = age;
		this.rollNumber = rollNumber;
		this.department = department;
	}

 public Long getId() {
	return id;
 }

 public void setId(Long id) {
	this.id = id;
 }


 public UserSelf getUser() {
	return user;
 }

 public void setUser(UserSelf user) {
	this.user = user;
 }

 public int getAge() {
	return age;
 }

 public void setAge(int age) {
	this.age = age;
 }

 public String getRollNumber() {
	return rollNumber;
 }

 public void setRollNumber(String rollNumber) {
	this.rollNumber = rollNumber;
 }

 public String getDepartment() {
	return department;
 }

 public void setDepartment(String department) {
	this.department = department;
 }
    
    
    
}


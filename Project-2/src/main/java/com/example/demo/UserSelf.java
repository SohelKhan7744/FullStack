package com.example.demo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "Users")
public class UserSelf {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
     private Long id;
     private String username;
     @Pattern(
    	        regexp = "^[0-9]{10}$",
    	        message = "Phone number must be 10 digits")
     private String phone;
      @Column(unique = true)
     private String email;
      
     private String password;
     private String image_url;
     
     @Column
     private LocalDateTime lastLogin;
     
     @Column
     private LocalDateTime lastSeen;
     
     @Enumerated(EnumType.STRING)
     private Role role;
     
     
	 

	 protected UserSelf(Long id, String username,
			@Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits") String phone, String email,
			String password, String image_url, LocalDateTime lastLogin,LocalDateTime lastSeen, Role role) {
		super();
		this.id = id;
		this.username = username;
		this.phone = phone;
		this.email = email;
		this.password = password;
		this.image_url = image_url;
		this.lastLogin = lastLogin;
		this.role = role;
		this.lastSeen = lastSeen;
	}


	 public Long getId() {
		 return id;
	 }


	 public void setId(Long id) {
		 this.id = id;
	 }


	 public String getUsername() {
		 return username;
	 }


	 public void setUsername(String username) {
		 this.username = username;
	 }

	 public String getImage_url() {
		 return image_url;
	 }


	 public void setImage_url(String image_url) {
		 this.image_url = image_url;
	 }

	 public String getPhone() {
		 return phone;
	 }



	 public void setPhone(String phone) {
		 this.phone = phone;
	 }

	 public String getEmail() {
		 return email;
	 }


	 public void setRole(Role role) {
		 this.role = role;
	 }

	 public Role getRole() {
		 return role;
	 }


	 public void setEmail(String email) {
		 this.email = email;
	 }


	 public String getPassword() {
		 return password;
	 }


	 public void setPassword(String password) {
		 this.password = password;
	 }
	  
	 public LocalDateTime getLastLogin() {
		return lastLogin;
	}


	 public void setLastLogin(LocalDateTime lastLogin) {
		 this.lastLogin = lastLogin;
	 }
	 

	 public LocalDateTime getLastSeen() {
		return lastSeen;
	}


	 public void setLastSeen(LocalDateTime lastSeen) {
		 this.lastSeen = lastSeen;
	 }


	 UserSelf(){
		 
	 }
       
}

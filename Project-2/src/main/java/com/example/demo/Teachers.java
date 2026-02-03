package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "teachers")
public class Teachers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private UserSelf user;
    
    @NotNull
    @Min(0)
    private Integer experience;

    private String qualifications;
    
    Teachers(){
    	
    }

	protected Teachers(Long id, UserSelf user, Integer experience,
			String qualifications) {
		super();
		this.id = id;
	
		this.user = user;
		this.experience = experience;
		this.qualifications = qualifications;
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

	public int getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public String getQualifications() {
		return qualifications;
	}

	public void setQualifications(String qualifications) {
		this.qualifications = qualifications;
	}
     
	
   }

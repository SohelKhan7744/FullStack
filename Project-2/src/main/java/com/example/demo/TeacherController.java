package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/teachers")
public class TeacherController {
           
	@Autowired
	private TeachersRepository teacherRepo;
	
	@Autowired
	private CourseRepository courseRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	 @Autowired
	 private CourseService courseService;
	
	@PostMapping("/profile")
	@PreAuthorize("hasRole('TEACHER')")
	public ResponseEntity<?>
	createProfile(@RequestBody Teachers teacher){
		
		String username = SecurityContextHolder
				             .getContext()
				             .getAuthentication()
				             .getName();
		
		UserSelf user = userRepo.findByUsername(username).
				orElseThrow(()-> new RuntimeException("User not Found"));
		
		if(teacherRepo.findByUser(user).isPresent()) {
			 
			return ResponseEntity
					.badRequest()
					.body("Teacher profile already exists");
		}
		
		             teacher.setUser(user);
		             teacherRepo.save(teacher);
		             
		             return ResponseEntity.ok("Teacher profile created successfully");
	}
	
	    @GetMapping("/profile")
	    @PreAuthorize("hasRole('TEACHER')")
	    public ResponseEntity<?>
	    getProfile(){
	    	       
	    	         String username = SecurityContextHolder
	    	        		 .getContext()
	    	        		 .getAuthentication()
	    	        		 .getName(); 
	    	         
	    	         UserSelf user = userRepo.findByUsername(username)
	    	        		              .orElseThrow(()-> new RuntimeException("User not found"));
	    	         
	    	         Teachers teacher = teacherRepo.findByUser(user)
	    	        		 .orElseThrow(()-> new RuntimeException("Teacher profile not found"));
	    	         
	    	         return ResponseEntity.ok(teacher);
	    }
	    
	    @GetMapping("/courses")
	    public List<Course> myCourses() {
	        String username = SecurityContextHolder
	                .getContext().getAuthentication().getName();

	        return courseService.getTeacherCourse(username);
	    }
	    
	   
}

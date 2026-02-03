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

@RestController
@RequestMapping("/students")

public class StudentController {

	@Autowired
	private StudentsRepository studentRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
    private EnrollmentService enrollmentService;
	
	@Autowired
	private EnrollmentRepository enrollmentRepo;
	
	@PostMapping("/profile")
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<?>
	createProfile(@RequestBody Students student){
		
		System.out.println(
				  SecurityContextHolder.getContext()
				    .getAuthentication()
				    .getAuthorities()
				);

		String username = SecurityContextHolder
				            .getContext()
				            .getAuthentication()
				            .getName();
		
		UserSelf user = userRepo.findByUsername(username).orElseThrow(()->
		                new RuntimeException("User not found"));
		
		if(studentRepo.findByUser(user).isPresent()) {
			 
			return ResponseEntity
					.badRequest()
					.body("Sudent profile already exists");
		}
		student.setUser(user);
		
		studentRepo.save(student);
		
		return ResponseEntity.ok("Student profile created successfully");
	}
	
	@GetMapping
	public ResponseEntity<?>
	getProfile(){
		String username =SecurityContextHolder
				           .getContext()
				           .getAuthentication()
				           .getName();
		
		UserSelf user = userRepo.findByUsername(username).orElseThrow(()-> new RuntimeException
				        ("User Not Found"));
		
		Students student = studentRepo.findByUser(user).orElseThrow(()-> new RuntimeException
				("Student Not Found"));
		
		return ResponseEntity.ok(student);
	}
	
    @PostMapping("/enroll")
    public ResponseEntity<?> enroll(@RequestBody EnrollDTO dto) {

        String username = SecurityContextHolder
                .getContext().getAuthentication().getName();
        

        return ResponseEntity.ok(
                enrollmentService.enrollStudent(username, dto.courseId)
        );
    }

    // VIEW MY COURSES
    @GetMapping("/mycourses")
    public List<Course> myCourses() {

        String username = SecurityContextHolder
                .getContext().getAuthentication().getName();

        return enrollmentService.getStudentCourses(username);
    }
    
    public List<Students> getAllStudents(){
    	  return studentRepo.findAll();
    }
}

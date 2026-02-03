package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

@Service
public class EnrollmentService {
     
	@Autowired
	private EnrollmentRepository enrollmentRepo;
	
	@Autowired
	private StudentsRepository studentRepo;
	
	@Autowired
	private CourseRepository courseRepo;
	
	@Transactional
	public String enrollStudent(String username,Long courseId) {
		      
		  Students student = studentRepo.findByUser_Username(username)
                .orElseThrow(()-> new RuntimeException("Student profile not found"));
		  
		  Course course = courseRepo.findById(courseId)
				  .orElseThrow(()-> new RuntimeException("Course not found"));
		  
		  if (enrollmentRepo.existsByStudentAndCourse(student, course)) {
		        throw new ResponseStatusException(
		            HttpStatus.CONFLICT,
		            "Already enrolled"
		        );
		    }
		  
		  Enrollment enrollment = new Enrollment();
		  
		  enrollment.setStudent(student);
		    enrollment.setCourse(course);
		    
		    enrollmentRepo.save(enrollment);
		    
		    return "Enrollment successfull";
	}
	
	public void adminEnroll(String studentUsername,Long courseId) {
		
		Students student = studentRepo.findByUser_Username(studentUsername).orElseThrow();
		
		Course course = courseRepo.findById(courseId).orElseThrow();
		
		if(!enrollmentRepo.existsByStudentAndCourse(student, course))
		{ 
			Enrollment e = new Enrollment();
			            e.setStudent(student);
			            e.setCourse(course);
			            enrollmentRepo.save(e);
		}
	}
	
	public List<Course> getStudentCourses(String username) {

	    Students student = studentRepo.findByUser_Username(username)
	        .orElseThrow(() -> new RuntimeException("Student not found"));

	    return enrollmentRepo.findByStudent(student)
	        .stream()
	        .map(Enrollment::getCourse)
	        .toList();
	}

}

package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CourseService {
           
	@Autowired
	private CourseRepository courseRepo;
	
	@Autowired
	private EnrollmentRepository enrollmentRepo;
	
	@Autowired
	private TeachersRepository teacherRepo;
	
	public Course createCourse(Course course) {
		 return courseRepo.save(course);
	}
	
	public List<Course> multiCourses(List<Course> course) {
		return courseRepo.saveAll(course);
	}
	           
	public Course assignTeacher(AssignTeacherDTO dto) {
		  
		Course course = courseRepo.findById(dto.courseId)
				.orElseThrow(()-> new RuntimeException("Teacher not found"));
		
		Teachers teacher = teacherRepo.findByUser_Username(dto.teacherUsername)
				.orElseThrow(()-> new RuntimeException("Teacher not found"));
		
		if (teacher.getUser().getRole() != Role.TEACHER) {
		    throw new RuntimeException("User is not a TEACHER");
		}

		
		 course.setTeacher(teacher);
		 return courseRepo.save(course);
	}
	
	public List<Course>
	getTeacherCourse(String username){
		
	return courseRepo.findByTeacher_User_Username(username);
		
	}
	 public List<Course>
	  getAllCourse(){
		 return courseRepo.findAll();
	 }
	 
	 @Transactional
	    public Course assignCourseToSelf(Long courseId, String username) {

	        Course course = courseRepo.findById(courseId)
	                .orElseThrow(() -> new RuntimeException("Course not found"));

	        Teachers teacher = teacherRepo.findByUser_Username(username)
	                .orElseThrow(() -> new RuntimeException("Teacher not found"));

	        // Optional safety: prevent overwrite
	        if (course.getTeacher() != null) {
	            throw new RuntimeException("Course already assigned to a teacher");
	        }

	        course.setTeacher(teacher);
	        return courseRepo.save(course);
	    }
        
	 public List<Students> getStudentsOfCourse(Long courseId, String username) {

		    Course course = courseRepo.findById(courseId)
		            .orElseThrow(() -> new RuntimeException("Course not found"));

		    // ownership check
		    if (course.getTeacher() == null ||
		        !course.getTeacher().getUser().getUsername().equals(username)) {
		        throw new RuntimeException("You are not allowed to view this course");
		    }

		    return enrollmentRepo.findByCourseId(courseId)
		            .stream()
		            .map(Enrollment::getStudent)
		            .toList();
		}
	 
	 public Course updateCourse(Long id, CourseUpdateRequest request) {

	        Course course = courseRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Course not found"));

	        if (request.getTitle() == null || request.getTitle().isBlank()) {
	            throw new RuntimeException("Title is required");
	        }

	        course.setTitle(request.getTitle());
	        course.setDescription(request.getDescription());

	        return courseRepo.save(course);
	    }

}

package com.example.demo;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserSelf,Long> {
	
	@Query("SELECT COUNT(u) FROM UserSelf u WHERE u.lastLogin >= :time")
	long countActiveUsers(@Param("time") 
	LocalDateTime time);
	
	@Query("SELECT COUNT(u) FROM UserSelf u WHERE u.lastSeen >= :time")
	long countOnlineUsers(@Param("time") 
	LocalDateTime time);
	
      Optional<UserSelf>findByUsername(String username);
      
      boolean existsByUsername(String Username);
      
      Optional<UserSelf>findByEmail(String email);
      
}
      interface StudentsRepository extends JpaRepository<Students,Long>{
    	  
    	  
    	  Optional<Students> findByUser(UserSelf user);

    	    Optional<Students> findByUser_Username(String username);
    
    	  Page<Students> findAll(Pageable pageable);
    	 
    	   
      }
       interface TeachersRepository extends JpaRepository<Teachers,Long>{
    	   
    	   Optional<Teachers>findByUser(UserSelf user);
    	   
    	   Optional<Teachers>
    	   findByUser_Username(String username);

    	   
    	   

       }
       
       interface CourseRepository extends JpaRepository<Course,Long>{
    	   
    	    List<Course>
    	    findByTeacher_User_Username(String username);
       }
       
       interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    	    boolean existsByStudentAndCourse(Students student, Course course);
    	    
    	    List<Enrollment> findByCourseId(Long courseId);

    	    Optional<Enrollment> findByStudentAndCourse(Students student, Course course);

    	    List<Enrollment> findByStudent(Students student);

    	    List<Enrollment> findByCourse(Course course);

    	    @Query("""
    	       select e.course
    	       from Enrollment e
    	       where e.student.user.username = :username
    	    """)
    	    List<Course> findCoursesByStudentUsername(String username);
    	}

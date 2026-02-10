package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CourseController {

    @Autowired
    private CourseService courseService;
    
    @Autowired
    private CourseRepository courseRepo;

    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return courseService.getAllCourse();
    }
    
    @PostMapping("/teachers/assign-course/{courseId}")
    public Course assignCourseToSelf(@PathVariable Long courseId) {

        String username = SecurityContextHolder
                .getContext().getAuthentication().getName();

        return courseService.assignCourseToSelf(courseId, username);
    }

    @GetMapping("/teachers/courses/{courseId}/students")
    @PreAuthorize("hasRole('TEACHER')")
    public List<Students> getStudentsOfCourse(@PathVariable Long courseId) {

        String username = SecurityContextHolder
                .getContext().getAuthentication().getName();

        return courseService.getStudentsOfCourse(courseId, username);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {
        return courseRepo.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE course
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseUpdateRequest request) {

        Course updated = courseService.updateCourse(id, request);
        return ResponseEntity.ok(updated);
    }

}

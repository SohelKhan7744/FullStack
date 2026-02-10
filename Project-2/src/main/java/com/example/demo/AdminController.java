package com.example.demo;

import java.time.LocalDateTime;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private StudentsRepository studentRepo;

    @Autowired
    private TeachersRepository teacherRepo;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private CourseRepository courseRepo;
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    @Autowired
    private EnrollmentRepository enrollmentRepo;

  
    @GetMapping("/dashboard")
    public Map<String, Long> dashboard() {
        Map<String, Long> data = new HashMap<>();
        data.put("students", studentRepo.count());
        data.put("teachers", teacherRepo.count());
        data.put("courses", courseRepo.count());
        data.put("enrollment", enrollmentRepo.count());
        
        LocalDateTime last24h = LocalDateTime.now().minusHours(24);
        data.put("visitors", userRepo.countActiveUsers(last24h));
        
        LocalDateTime last5min = LocalDateTime.now().minusMinutes(5);
        data.put("onlineNow", userRepo.countOnlineUsers(last5min));
        return data;
    }

 
    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@RequestBody CreateStudentDTO dto) {

        UserSelf user = userRepo.findByUsername(dto.username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.STUDENT) {
            return ResponseEntity.badRequest().body("User is not a STUDENT");
        }

        Students student = new Students();
        student.setUser(user);
        student.setAge(dto.age);
        student.setRollNumber(dto.rollNumber);
        student.setDepartment(dto.department);

        studentRepo.save(student);
        return ResponseEntity.ok("Student profile created");
    }

    @GetMapping("/students")
    public List<Students> getAllStudents() {
        return studentRepo.findAll();
    }
   @GetMapping("/student")
    public ResponseEntity<Map<String,Object>>
    getStudents(@RequestParam(defaultValue ="1") int page,
    		@RequestParam(defaultValue = "10" )int limit){
    	  
    	Pageable pageable = PageRequest.of(page - 1, limit);
    	 Page<Students> studentPage = studentRepo.findAll(pageable);
    	 
    	 List<StudentResponseDTO> data = studentPage
    			                      .getContent()
    			                        .stream()
    			                        .map(s -> new StudentResponseDTO(
    			                        		s.getId(),
    			                        		s.getUser().getUsername(),
    			                        		s.getUser().getEmail(),
    			                        		s.getDepartment(),
    			                        		s.getAge(),
    			                        		s.getRollNumber()
    			                        		)).toList();
    	 
    	 Map<String,Object> response = new HashMap<>();
    	 response.put("data",data);
    	 response.put("total", studentPage.getTotalElements());
    	 response.put("page", page);
    	 response.put("limit", limit);
    	 
    	 return  ResponseEntity.ok(response);
    }
  
   @GetMapping("/student/{id}")
   public ResponseEntity<UpdateStudentDTO> getStudentForEdit(@PathVariable Long id) {

       Students s = studentRepo.findById(id)
           .orElseThrow(() -> new RuntimeException("Student not found"));

       UserSelf u = s.getUser();

       UpdateStudentDTO dto = new UpdateStudentDTO();
       dto.username = u.getUsername();
       dto.email = u.getEmail();
       dto.phone = u.getPhone();
       dto.image_url = u.getImage_url();
       dto.age = s.getAge();
       dto.department = s.getDepartment();
       dto.rollNumber = s.getRollNumber();

       return ResponseEntity.ok(dto);
   }

   @Transactional
   @PutMapping("/student/{id}")
   public ResponseEntity<?> updateStudent(
           @PathVariable Long id,
           @RequestBody UpdateStudentDTO dto
   ) {
       Students student = studentRepo.findById(id)
           .orElseThrow(() -> new RuntimeException("Student not found"));

       UserSelf user = student.getUser();

       // 🔹 Update UserSelf
       user.setUsername(dto.username);
       user.setEmail(dto.email);
       user.setPhone(dto.phone);
       user.setImage_url(dto.image_url);
       userRepo.save(user);

       // 🔹 Update Student
       student.setAge(dto.age);
       student.setDepartment(dto.department);
       student.setRollNumber(dto.rollNumber);
       studentRepo.save(student);

       return ResponseEntity.ok("Student updated successfully");
   }


   
    @PostMapping("/teachers")
    public ResponseEntity<?> createTeacher(@RequestBody CreateTeacherDTO dto) {

        UserSelf user = userRepo.findByUsername(dto.username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.TEACHER) {
            return ResponseEntity.badRequest().body("User is not a Teacher");
        }
        if (teacherRepo.findByUser(user).isPresent()) {
            return ResponseEntity.badRequest().body("Teacher profile already exists");
        }

        Teachers teacher = new Teachers();
        teacher.setUser(user);
        teacher.setExperience(dto.experience);
        teacher.setQualifications(dto.qualification);
     

        teacherRepo.save(teacher);
        return ResponseEntity.ok("Teacher profile created");
    }

    @GetMapping("/teachers")
    public List<Teachers> getAllTeachers() {
        return teacherRepo.findAll();
    }
    
    @PostMapping("/courses")
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }
    
    @PostMapping("/multi/courses")
    public List<Course> multiCourse(@RequestBody List<Course> course){
    	    return courseService.multiCourses(course);
    }

    // ASSIGN TEACHER TO COURSE
    @PostMapping("/courses/assign-teacher")
    public Course assignTeacher(@RequestBody AssignTeacherDTO dto) {
        return courseService.assignTeacher(dto);
    }

    // ADMIN enrolls student
    @PostMapping("/enroll")
    public ResponseEntity<?> enrollStudent(@RequestBody AdminEnrollDTO dto) {
        enrollmentService.adminEnroll(dto.studentUsername, dto.courseId);
        return ResponseEntity.ok("Student enrolled");
    }
    
    @GetMapping("/teacher")
    public ResponseEntity<Map<String, Object>> getTeachers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Teachers> teacherPage = teacherRepo.findAll(pageable);

        List<Map<String, Object>> data = teacherPage.getContent()
            .stream()
            .map(t -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", t.getId());
                m.put("username", t.getUser().getUsername());
                m.put("email", t.getUser().getEmail());
                m.put("qualifications", t.getQualifications());
                m.put("experience", t.getExperience());
                return m;
            })
            .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("data", data);
        response.put("total", teacherPage.getTotalElements());
        response.put("page", page);
        response.put("limit", limit);

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/teacher/{id}")
    public ResponseEntity<UpdateTeacherDTO> getTeacherForEdit(@PathVariable Long id) {

        Teachers t = teacherRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Teacher not found"));

        UserSelf u = t.getUser();

        UpdateTeacherDTO dto = new UpdateTeacherDTO();
        dto.username = u.getUsername();
        dto.email = u.getEmail();
        dto.phone = u.getPhone();
        dto.image_url = u.getImage_url();
        dto.experience = t.getExperience();
        dto.qualification = t.getQualifications();

        return ResponseEntity.ok(dto);
    }

    @Transactional
    @PutMapping("/teacher/{id}")
    public ResponseEntity<?> updateTeacher(
            @PathVariable Long id,
            @RequestBody UpdateTeacherDTO dto
    ) {
    	Teachers teacher = teacherRepo.findById(id)
    .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Teacher not found"
    ));

UserSelf user = teacher.getUser();

Map<String, String> errors = new HashMap<>();

//🔹 Email uniqueness check (ignore current user)
userRepo.findByEmail(dto.email)
    .filter(existing -> !existing.getId().equals(user.getId()))
    .ifPresent(existing -> errors.put("email", "Email already exists"));

//🔹 Phone validation
if (dto.phone == null || !dto.phone.matches("\\d{10}")) {
errors.put("phone", "Phone number must be exactly 10 digits");
}

//🔹 Experience validation
if (dto.experience < 0) {
errors.put("experience", "Experience must be a positive number");
}

//🔹 Stop if errors exist
if (!errors.isEmpty()) {
return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(Map.of("errors", errors));
}

//🔹 Update UserSelf
user.setEmail(dto.email);
user.setPhone(dto.phone);
user.setImage_url(dto.image_url);
userRepo.save(user);

//🔹 Update Teacher
teacher.setExperience(dto.experience);
teacher.setQualifications(dto.qualification);
teacherRepo.save(teacher);

return ResponseEntity.ok("Teacher updated successfully");

    }

}

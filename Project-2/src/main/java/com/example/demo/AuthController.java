package com.example.demo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/register")
	public ResponseEntity<?>
	register(@RequestBody UserSelf user) {
		if(userRepo.findByUsername(user.getUsername()).isPresent()) {
			return ResponseEntity
					.badRequest()
					.body("Username already exists");
		}
         
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		if(user.getRole() == null) {
			user.setRole(Role.STUDENT);
		}
		 userRepo.save(user);
		 
		 return ResponseEntity.ok("User registered successfully");
		     
	}
	@PostMapping("/login")
	public ResponseEntity<?>
	login(@RequestBody UserSelf user){
		 
		UserSelf userdb = userRepo.findByUsername(user.getUsername())
				.orElseThrow(()-> new RuntimeException("User not found"));
		
		
		if(!passwordEncoder.matches(user.getPassword(), userdb.getPassword())) {
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body("Invaild credentials");
		}
		
		userdb.setLastLogin(LocalDateTime.now());
		
		userRepo.save(userdb);
		
		 String token = jwtUtil.generateToken(userdb.getUsername(),
				                               userdb.getRole().name());
		
		
		
		Map<String,Object> response = new HashMap<>();
		response.put("token", token);
		response.put("username", userdb.getUsername());
		response.put("role",userdb.getRole());
		
		return ResponseEntity.ok(response);
	}	
}

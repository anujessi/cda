package org.jsp.CDA.controller;

import org.jsp.CDA.DTO.Authantication;
import org.jsp.CDA.entity.User;
import org.jsp.CDA.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/users") // Optional, to group all user-related endpoints under '/users'
public class UserController {

	@Autowired
	private UserService userService;
    @Operation(summary="This API will save the user data")
	@PostMapping
	public ResponseEntity<?> saveUser(@RequestBody User user) {
		return userService.saveUser(user);

	}

	@GetMapping("/findall")
	public ResponseEntity<?> findAllUsers() {
		return userService.findAllUsers();
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<?> findUserById(@PathVariable int id) {
		return userService.findUserById(id);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Authantication auth) {

		return userService.login(auth);
	}
	
	@PatchMapping("/{id}/otp/{otp}")
	public ResponseEntity<?> verifyOtp(@PathVariable int id,@PathVariable int otp){
		return userService.verifyOtp(id,otp);
	}
	

	@DeleteMapping("/{id}")
	public String deleteUserById(@PathVariable int id) {
		return userService.deletUserById(id);
	}

	
}

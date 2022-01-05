package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.UserEntity;
import com.example.demo.services.UserService;

/**
 * CRUDController class.
 * This class handle petitions for User's CRUD operations.
 * @author: Rinegal1218 
 */
@RestController
@RequestMapping("/crud-controller")
public class CRUDController {
	
	//Dependency injection for UserService.
	@Autowired
	private UserService userService;
	
	//Dependency injection for input data validation.
	@Autowired
	private Validator validator;
	
	
	/**
	 * This method is used to return all users in database.
	 * @Type GET
	 * @return ResponseEntity with a list of all user in database.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		
		Map<String, Object> response = new HashMap<>();
		
		//Call to service for searching all users.
		List<UserEntity> allUsers = userService.getAllUsers();
		
		//If we don't have any user record, we return a message. In other case, return a list of users.
		if(allUsers.isEmpty()) {
			response.put("message", "not users in database!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		} else {
			response.put("users", allUsers);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		}
	}
	
	/**
	 * This method is used to find one user by id in database.
	 * @Type GET
	 * @param Long PathVariable UserEntity id.
	 * @return ResponseEntity with the user found.
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<?> getOne(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		//Call to service for searching a user by id.
		UserEntity user = userService.getUserById(id);
		
		//If service returns a record, we send it back. In other case, return a message.
		if(user != null) {
			response.put("user", user);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} else {
			response.put("message", "user not found!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);	
		}
	}
	
	/**
	 * This method is used to find one or more users in database.
	 * @Type POST. (A POST method is used because we need ids in request body of petition).
	 * @param List<Long> with users id to search.
	 * @return ResponseEntity with users found.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/get-users")
	public ResponseEntity<?> getManyUsers(@RequestBody List<Long> usersId) {
		
		System.out.println("get users: " + usersId);
		Map<String, Object> response = new HashMap<>();
		
		List<UserEntity> users = userService.getUsersById(usersId);
		response.put("users_found", users);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	/**
	 * This method is used for creating a new user record.
	 * @Type POST
	 * @param UserEntity object.
	 * @param BindingResult object for validations
	 * @return ResponseEntity with new user created.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addUser(@Valid @RequestBody UserEntity user, BindingResult bindingResult) {
		List<String> error = new ArrayList<String>();
		Map<String, Object> response = new HashMap<>();
		
		//@Valid and BindingResult objects are used to validate incoming information, the validations are defined in UserEntity class definition.
		//If BindingResult find errors, return an array of strings with its.
		if(bindingResult.hasErrors()) {
			
			//We convert bindingResult field errors in a stream to modify error messages.
			error = bindingResult.getFieldErrors()
					.stream()
					.map(e -> "Field " + e.getField() + ": " + e.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", error);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		//If any error is found, call to service for creating a new user record.
		UserEntity userAdded = userService.createUser(user);
		
		response.put("new_user", userAdded);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	/**
	 * This method is used for updating data of an existing user record.
	 * @Type: PUT
	 * @param Long PathVariable with user id.
	 * @param UserEntity object.
	 * @param BindingResult object.
	 * @return ResponseEntity with UserEntity updated.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserEntity user, BindingResult bindingResult) {
		List<String> error = new ArrayList<String>();
		Map<String, Object> response = new HashMap<>();
		
		//@Valid and BindingResult object are used to validate incoming information, the validations are defined in UserEntity class definition.
		//If BindingResult find errors, return an array of strings with its.
		if(bindingResult.hasErrors()) {
			
			//We convert bindingResult field errors in a stream to modify error messages.
			error = bindingResult.getFieldErrors()
					.stream()
					.map(e -> "Field " + e.getField() + ": " + e.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", error);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		//If any error is found, call to service for creating a new user record.
		UserEntity userModified = userService.updateUser(id, user);
		
		if(userModified != null) {
			response.put("user_modified", userModified);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
		} else {
			response.put("message", "user does not exist!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * This method is used for creating one or more users in one petition.
	 * @Type POST
	 * @param List<UserEntity>.
	 * @param BindingResult object.
	 * @return RequestEntity with new users created.
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/add-users")
	public ResponseEntity<?> addManyUsers(@RequestBody List<UserEntity> users, BindingResult bindingResult) {
		
		List<String> error = new ArrayList<String>();
		Map<String, Object> response = new HashMap<>();
		
		//We need a set for errors.
		Set<ConstraintViolation<UserEntity>> violations = new HashSet<>();
		
		
		for(int i = 0; i < users.size(); i++) {
			
			//Pass user object through validator.
			violations = validator.validate(users.get(i));
			
			
			for (ConstraintViolation<UserEntity> violation : violations) {
		        String propertyPath = violation.getPropertyPath().toString();
		        String message = violation.getMessage();
		        // Add JSR-303 errors to BindingResult
		        // This allows Spring to display them in view via a FieldError
		        bindingResult.addError(new FieldError("user", propertyPath,
		                "Object " + (i + 1) + " " + message));
		    }
		 
			//If BindingResult find errors, return an array of strings with its.
			if(bindingResult.hasErrors()) {
				error = bindingResult.getFieldErrors()
						.stream()
						.map(e -> "Field " + e.getField() + ": " + e.getDefaultMessage())
						.collect(Collectors.toList());
			}
			
		}
		
		//If any error is found, call to service for creating a users records. In other case, return errors.
		if(error.isEmpty()) {
			List<UserEntity> usersAdded = userService.createUsers(users);
			response.put("users_added", usersAdded);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} else {
			response.put("errors", error);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * This method is used for updating data of one or more users in database.
	 * @Type PUT
	 * @param List<UserEntity> with users data.
	 * @param BindingResult object.
	 * @return RequestEntity with users updated.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/modify-users")
	public ResponseEntity<?> updateManyUsers(@RequestBody List<UserEntity> users, BindingResult bindingResult) {
		
		List<String> error = new ArrayList<String>();
		Map<String, Object> response = new HashMap<>();
		
		//We need a set for errors.
		Set<ConstraintViolation<UserEntity>> violations = new HashSet<>();
		
		for(int i = 0; i < users.size(); i++) {
			
			//Pass user object through validator.
			violations = validator.validate(users.get(i));
			
			
			for (ConstraintViolation<UserEntity> violation : violations) {
				
		        String propertyPath = violation.getPropertyPath().toString();
		        String message = violation.getMessage();
		        
		        // Add JSR-303 errors to BindingResult
		        // This allows Spring to display them in view via a FieldError
		        bindingResult.addError(new FieldError("user", propertyPath,
		                "Object " + (i + 1) + " " + message));
		    }
			
			//If BindingResult find errors, return an array of strings with its.
			if(bindingResult.hasErrors()) {
				error.addAll(bindingResult.getFieldErrors()
						.stream()
						.map(e -> "Field " + e.getField() + ": " + e.getDefaultMessage())
						.collect(Collectors.toList()));
			}
			
			if(users.get(i).getId() == null) {
				error.add("Field id: Object " + (i + 1) + " id cannot be empty");
			}
			
			System.out.println(error);
		}
		
		//If any error is found, call to service for updating a users records. In other case, return errors.
		if(error.isEmpty()) {
			List<UserEntity> usersModified = userService.createUsers(users);
			response.put("users_modified", usersModified);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} else {
			response.put("errors", error);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	/**
	 * This method is used for deleting data of one user in database.
	 * @Type DELETE
	 * @param Long PathVariable with user id.
	 * @return RequestEntity with a message.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		
		Map<String, Object> response = new HashMap<>();
		
		//If user exits in database, we delete it and return a message. In other case, return a message of user not found.
		if(userService.deleteUser(id)) {
			response.put("message", "user deleted!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} else {
			response.put("message", "user not found!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * This method is used for deleting data of one or more users in database.
	 * @Type DELETE
	 * @param List<Long> with ids of users to be deleted.
	 * @return RequestEntity with a message.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete-users")
	public ResponseEntity<?> deleteManyUsers(@RequestBody List<Long> usersIds) {
		
		Map<String, Object> response = new HashMap<>();
		
		//Call service method.
		if(userService.deleteUsers(usersIds)) {
			
			response.put("message", "users deleted!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} else {
			//We return a internal server error if service method fails. 
			response.put("message", "internal server error!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * This method is used for deleting data of all users in database.
	 * @Type DELETE
	 * @return RequestEntity with a message.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete-all")
	public ResponseEntity<?> deleteAllUsers() {
		Map<String, Object> response = new HashMap<>();
		
		//Call service method.
		if(userService.deleteAllUsers()) {
			response.put("message", "all users have been deleted!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} else {
			
			//We return a internal server error if service method fails.
			response.put("message", "internal server error!");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

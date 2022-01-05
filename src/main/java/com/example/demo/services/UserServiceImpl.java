package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.UserRepository;

/**
 * UserServiceImpl class.
 * Implements UserService interface.
 * @author: Rinegal1218
 */
@Service
public class UserServiceImpl implements UserService {
	
	//Dependency injection for UserRepository.
	@Autowired
	private UserRepository userRepository;

	/**
	 * This method returns all users in database.
	 * @return List<UserEntity> with all users in database.
	 */
	@Override
	public List<UserEntity> getAllUsers() {
		
		//Call repository to find all users.
		return userRepository.findAll();
		
	}

	/**
	 * This method returns one user by id.
	 * @param Long with user id.
	 * @return UserEntity object
	 */
	@Override
	public UserEntity getUserById(Long id) {
		
		try {
			//Call repository to find user by id.
			return userRepository.findById(id).get();
		} catch(Exception e) {
			System.out.println("ERROR");
			return null;
		}
	}
	
	/**
	 * This method returns users by id.
	 * @param List<Long> with users id to find in database.
	 * @return List<UserEntity> with users found.
	 */
	@Override
	public List<UserEntity> getUsersById(List<Long> ids) {
		
		//Call repository to find users by id.
		return userRepository.findAllById(ids);
	}

	/**
	 * This method creates a new user in database.
	 * @param UserEntity object (not including id).
	 * @return UserEntity object (created user object).
	 */
	@Override
	public UserEntity createUser(UserEntity user) {
		
		//Call repository to create user record.
		UserEntity userAdded = userRepository.save(user);
		return userAdded;
		
	}

	/**
	 * This method updates a user record of database.
	 * @param Long user id.
	 * @param UserEntity object with new data.
	 * @return UserEntity object (user with new data in database).
	 */
	@Override
	public UserEntity updateUser(Long id, UserEntity user) {
		
		try {
			//First, we find user record by id.
			UserEntity userToUpdate = userRepository.getById(id);
			
			//We set all new information.
			userToUpdate.setName(user.getName());
			userToUpdate.setAge(user.getAge());
			
			//Finally, we save user record with new information in database.
			UserEntity userUpdated = userRepository.save(userToUpdate);
			return userUpdated;
		} catch(Exception e) {
			return null;
		}
		
	}
	
	/**
	 * This method creates users that contains list.
	 * @param List<UserEntity> with users information (not including).
	 * @return List<UserEntity> with the new users records.
	 */
	@Override
	public List<UserEntity> createUsers(List<UserEntity> users) {
		
		//Call repository to created users records.
		List<UserEntity> usersAdded = userRepository.saveAll(users);
		return usersAdded;
	}
	
	/**
	 * This method updates users that contains list.
	 * @param List<UserEntity> with users new information.
	 * @return List<UserEntity> with users records updated.
	 */
	@Override
	public List<UserEntity> updateUsers(List<UserEntity> users) {
		
		List<UserEntity> usersToModify = new ArrayList<>();
		
		for(int i = 0; i < users.size(); i++) {
			
			//We find users records one by one and set new data.
			UserEntity userToUpdate = userRepository.getById(users.get(i).getId());
			userToUpdate.setName(users.get(i).getName());
			userToUpdate.setAge(users.get(i).getAge());
			usersToModify.add(userToUpdate);
		}
		
		//Once every user record have the new information, we save all in database.
		List<UserEntity> usersModified = userRepository.saveAll(usersToModify);
		return usersModified;
	}

	/**
	 * This method deletes a user by id.
	 * @param Long user id.
	 * @return boolean flag to control.
	 */
	@Override
	public boolean deleteUser(Long id) {
		
		try {
			//Call repository to delete user by id.
			userRepository.deleteById(id);
			return true;
		} catch(Exception e) {
			return false;
		}	
	}
	
	/**
	 * This method deletes users by id.
	 * @param List<Long> with users ids.
	 * @return boolean flag to control.
	 */
	@Override
	public boolean deleteUsers(List<Long> usersIds) {
		try {
			//Call repository to delete many users.
			userRepository.deleteAllById(usersIds);
			return true;	
		} catch(Exception e) {
			return false;
		}
	}

	/**
	 * This method deletes all users records in database.
	 * @return boolean flag to control.
	 */
	@Override
	public boolean deleteAllUsers() {
		
		try {
			//Call repository to delete all users in database.
			userRepository.deleteAll();
			return true;
		} catch(Exception e) {
			return false;
		}
		
	}

}

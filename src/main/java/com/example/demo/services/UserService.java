package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.UserEntity;

/**
 * UserService interface.
 * This interface describes methods for its implementation.
 * @author: Rinegal1218
 *
 */
public interface UserService {
	
	public List<UserEntity> getAllUsers();
	public UserEntity getUserById(Long id);
	public List<UserEntity> getUsersById(List<Long> ids);
	public UserEntity createUser(UserEntity user);
	public UserEntity updateUser(Long id, UserEntity user);
	public List<UserEntity> createUsers(List<UserEntity> users);
	public List<UserEntity> updateUsers(List<UserEntity> users);
	public boolean deleteUser(Long id);
	public boolean deleteUsers(List<Long> usersIds);
	public boolean deleteAllUsers();
	

}

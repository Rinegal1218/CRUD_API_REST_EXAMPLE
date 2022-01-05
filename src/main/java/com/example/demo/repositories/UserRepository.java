package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.UserEntity;

/**
 * UserRepository interface.
 * This interface is used to inherit JpaRepository methods.
 * @author: Rinegal1218
 */

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

}

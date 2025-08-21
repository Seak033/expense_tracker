package com.serhat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.serhat.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);
	Optional<User> findByEmailIgnoreCase(String email);

}

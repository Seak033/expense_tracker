package com.serhat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.serhat.entity.Category;
import com.serhat.entity.User;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	boolean existsByUserAndNameIgnoreCase(User user, String name);
	Optional<Category> findByUserAndNameIgnoreCase(User user, String name);

	List<Category> findByUserAndActiveTrueOrderByNameAsc(User user);
	List<Category> findByUserOrderByNameAsc(User user);

	Optional<Category> findByIdAndUser(Long id, User user);
	List<Category> findByUserAndNameContainingIgnoreCase(User user, String q);
}
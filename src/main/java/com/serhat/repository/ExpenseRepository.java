package com.serhat.repository;

import java.time.LocalDate;
import java.util.*;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.serhat.entity.Expense;
import com.serhat.entity.User;

public interface ExpenseRepository extends JpaRepository<Expense, Long>{

	@EntityGraph(attributePaths = "category")
	List<Expense> findByUserOrderByDateDesc(User user);

	@EntityGraph(attributePaths = "category")
	List<Expense> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate start, LocalDate end);

	@EntityGraph(attributePaths = "category")
	List<Expense> findByUserAndCategory_IdOrderByDateDesc(User user, Long categoryId);
}

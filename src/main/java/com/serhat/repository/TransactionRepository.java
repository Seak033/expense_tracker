package com.serhat.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.serhat.entity.Transaction;
import com.serhat.entity.TransactionType;
import com.serhat.entity.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	@EntityGraph(attributePaths = "category")
	List<Transaction> findByUserOrderByDateDesc(User user);

	@EntityGraph(attributePaths = "category")
	List<Transaction> findByUserAndDateBetweenOrderByDateDesc(User user, LocalDate start, LocalDate end);

	@EntityGraph(attributePaths = "category")
	List<Transaction> findByUserAndCategory_IdOrderByDateDesc(User user, Long categoryId);

	@Query("SELECT COALESCE(SUM(tx.amount), 0) " +
			"FROM Transaction tx " +
			"WHERE tx.user = :user AND tx.type = :type")
	BigDecimal getTotalByType(@Param("user") User user, @Param("type") TransactionType type);

}

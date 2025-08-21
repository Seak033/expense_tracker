package com.serhat.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serhat.entity.Category;
import com.serhat.entity.Expense;
import com.serhat.entity.User;
import com.serhat.repository.CategoryRepository;
import com.serhat.repository.ExpenseRepository;

import jakarta.transaction.Transactional;

@Service
public class ExpenseService {

	@Autowired private ExpenseRepository expenses;
	@Autowired private CategoryRepository categories;

	@Transactional
	public Expense create(User user, BigDecimal amount, String description, LocalDate date, Long categoryId) {
		if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("Amount must be > 0");
		if (date == null) throw new IllegalArgumentException("Date is required");

		Category cat = categories.findById(categoryId)
				.orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Category not found"));
		if (!cat.getUser().getId().equals(user.getId()))
			throw new IllegalArgumentException("Category does not belong to user");
		if (!cat.isActive())
			throw new IllegalArgumentException("Category is disabled");

		Expense e = Expense.builder()
				.user(user).category(cat).amount(amount).description(description).date(date)
				.build();
		return expenses.save(e);
	}

	public List<Expense> List(User user) {
		return expenses.findByUserOrderByDateDesc(user);
	}
}

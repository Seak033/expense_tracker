package com.serhat.service;

import static java.util.stream.IntStream.rangeClosed;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serhat.entity.Category;
import com.serhat.entity.Transaction;
import com.serhat.entity.TransactionType;
import com.serhat.entity.User;
import com.serhat.repository.CategoryRepository;
import com.serhat.repository.TransactionRepository;
import com.serhat.web.MonthSummary;
import com.serhat.web.TransactionForm;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TransactionService {

	@Autowired private TransactionRepository transactions;
	@Autowired private CategoryRepository categories;

	@Transactional
	public Transaction create(User user, TransactionForm form) {
		if (form.getAmount() == null || form.getAmount().signum() <= 0) {
			throw new IllegalArgumentException("Amount must be > 0");
			}

		if (form.getDate() == null) {
			throw new IllegalArgumentException("Date is required");
			}

		Category cat = null;
		if (form.getType() == TransactionType.EXPENSE) {
			cat = categories.findById(form.getCategoryId())
				.orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Category not found"));
		if (!cat.getUser().getId().equals(user.getId()))
			throw new IllegalArgumentException("Category does not belong to user");
		if (!cat.isActive())
			throw new IllegalArgumentException("Category is disabled");
		}

		Transaction e = Transaction.builder()
				.user(user).category(cat).amount(form.getAmount()).description(form.getDescription())
				.date(form.getDate()).type(form.getType())
				.build();
		return transactions.save(e);
	}


	public Optional<Transaction> findByIdForUser(Long id, User user) {
		return transactions.findById(id)
				.filter(e -> e.getUser().getId().equals(user.getId()));
	}


	public List<Transaction> list(User user) {
		return transactions.findByUserOrderByDateDesc(user);
	}


	public BigDecimal totalByType(User user, TransactionType type) {
		return transactions.getTotalByType(user, type);
	}


	public BigDecimal getBalance(User user) {
		BigDecimal income = totalByType(user, TransactionType.INCOME);
		BigDecimal expense = totalByType(user, TransactionType.EXPENSE);
		return income.subtract(expense);
	}


	@Transactional
	public void update(User user, Long id, TransactionForm form) {
		Transaction tx = findByIdForUser(id, user)
				.orElseThrow(() -> new AccessDeniedException("Not allowed"));

		tx.setDate(form.getDate());
		tx.setAmount(form.getAmount());
		tx.setDescription(form.getDescription());
		tx.setType(form.getType());

		if (form.getType() == TransactionType.EXPENSE) {
			if (form.getCategoryId() == null) {
				throw new IllegalArgumentException("Category is required for expenses");
			}

		Category cat = categories.findById(form.getCategoryId())
				.orElseThrow(() -> new EntityNotFoundException("Category not found!"));
		if (!cat.getUser().getId().equals(user.getId())) {
			throw new IllegalArgumentException("Category does not belong to user");
		}

		if (!cat.isActive()) {
			throw new IllegalArgumentException("Category is disabled");
		}
		tx.setCategory(cat);
		}
	}


	@Transactional
	public void delete(User user, Long id) {
		Transaction tx = findByIdForUser(id, user)
				.orElseThrow(() -> new AccessDeniedException("Not allowed"));
		transactions.delete(tx);
	}


	public List<Transaction> list(User user, Integer year, Integer month, TransactionType typeFilter) {
		if (year == null && month == null && typeFilter == null) {
			// default list (existing method list
			return transactions.findByUserOrderByDateDesc(user);
		}

		LocalDate start;
		LocalDate end;
		if (year != null && month != null) {
			YearMonth ym = YearMonth.of(year, month);
			start = ym.atDay(1);
			end = ym.atEndOfMonth();
		} else if (year != null) {
			start = LocalDate.of(year, 1, 1);
			end = LocalDate.of(year, 12, 31);
		} else {
			// if only month is provided, assume current year
			YearMonth ym = YearMonth.of(LocalDate.now().getYear(), month);
			start = ym.atDay(1);
			end = ym.atEndOfMonth();
		}

		List<Transaction> base = transactions.findByUserAndDateBetweenOrderByDateDesc(user, start, end);
		if (typeFilter != null) {
			return base.stream()
					.filter(e -> e.getType() == typeFilter)
					.collect(Collectors.toList());
		}
		return base;
	}


	public List<MonthSummary> monthlyOverview(User user, int year) {
		LocalDate start = LocalDate.of(year, 1, 1);
		LocalDate end = LocalDate.of(year, 12, 31);
		List<Transaction> all = transactions.findByUserAndDateBetweenOrderByDateDesc(user, start, end);

		Map<Integer, List<Transaction>> byMonth = all.stream()
				.collect(Collectors.groupingBy(e -> e.getDate().getMonthValue()));

		List<MonthSummary> summaries =
				rangeClosed(1, 12)
				.mapToObj(m -> {
					List<Transaction> list = byMonth.getOrDefault(m, List.of());
					BigDecimal income = list.stream()
							.filter(e -> e.getType() == TransactionType.INCOME)
							.map(Transaction::getAmount)
							.reduce(BigDecimal.ZERO, BigDecimal::add);
					BigDecimal expense = list.stream()
							.filter(e -> e.getType() == TransactionType.EXPENSE)
							.map(Transaction::getAmount)
							.reduce(BigDecimal.ZERO, BigDecimal::add);
					return MonthSummary.builder().month(m).income(income).expense(expense).build();
				})
				.collect(Collectors.toList());
		return summaries;
	}
}

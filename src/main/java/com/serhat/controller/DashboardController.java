package com.serhat.controller;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.serhat.entity.Transaction;
import com.serhat.entity.TransactionType;
import com.serhat.entity.User;
import com.serhat.service.TransactionService;
import com.serhat.service.UserService;
import com.serhat.web.MonthSummary;

@Controller
public class DashboardController {

	@Autowired private UserService userService;
	@Autowired private TransactionService transactionService;

	@GetMapping("/dashboard")
	public String dashboard(Authentication auth, Model model,
			@RequestParam(required = false) Integer year,
			@RequestParam(required = false) Integer month,
			@RequestParam(required = false) TransactionType type) {

		User user = userService.getByEmail(auth.getName());

		// Default year
		int currentYear = LocalDate.now().getYear();
		int selectedYear = (year != null) ? year : currentYear;

		List<MonthSummary> overview = transactionService.monthlyOverview(user, selectedYear);

		List<Transaction> list = transactionService.list(user, year, month, type);

		model.addAttribute("transactions", list);
		model.addAttribute("totalIncome", transactionService.totalByType(user, TransactionType.INCOME));
		model.addAttribute("totalExpense", transactionService.totalByType(user, TransactionType.EXPENSE));
		model.addAttribute("balance", transactionService.getBalance(user));

		// Filter State
		model.addAttribute("overview", overview);
		model.addAttribute("selectedYear", selectedYear);
		model.addAttribute("selectedMonth", month);
		model.addAttribute("selectedType", type);

		List<Integer> years = IntStream.rangeClosed(currentYear - 4, currentYear)
				.boxed()
				.sorted(Comparator.reverseOrder())
				.toList();
		model.addAttribute("years", years);

		return "dashboard";
	}
}

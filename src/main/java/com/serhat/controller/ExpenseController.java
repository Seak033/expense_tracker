package com.serhat.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.serhat.entity.User;
import com.serhat.repository.CategoryRepository;
import com.serhat.service.ExpenseService;
import com.serhat.service.UserService;
import com.serhat.web.form.ExpenseForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

	@Autowired private UserService userService;
	@Autowired private ExpenseService expenseService;
	@Autowired private CategoryRepository categoryRepository;

	@GetMapping("/new")
	public String newExpense(Authentication auth, Model model) {
		User user = userService.getByEmail(auth.getName());

		if (!model.containsAttribute("expenseForm")) {
			ExpenseForm form = new ExpenseForm();
			form.setDate(LocalDate.now());
			model.addAttribute("expenseForm", form);
		}
		model.addAttribute("categories",
				categoryRepository.findByUserAndActiveTrueOrderByNameAsc(user));

		return "expense-form";
	}

	@PostMapping
	public String create(Authentication auth, @Valid @ModelAttribute("expenseForm") ExpenseForm form, BindingResult binding, RedirectAttributes ra) {
		if (binding.hasErrors()) {
			ra.addFlashAttribute("org.springframework.validation.BindingResult.expenseForm", binding);
			ra.addFlashAttribute("expenseForm", form);
			return "redirect:/expenses/new";
		}

		User user = userService.getByEmail(auth.getName());
		expenseService.create(user, form.getAmount(), form.getDescription(), form.getDate(), form.getCategoryId());

		ra.addFlashAttribute("succes", "Expanse added.");
		return "redirect:/dashboard";
	}
}

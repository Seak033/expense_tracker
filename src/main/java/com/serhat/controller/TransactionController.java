package com.serhat.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.serhat.entity.Transaction;
import com.serhat.entity.User;
import com.serhat.service.CategoryService;
import com.serhat.service.TransactionService;
import com.serhat.service.UserService;
import com.serhat.web.TransactionForm;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final CategoryService categoryService;

	@Autowired private UserService userService;
	@Autowired private TransactionService transactionService;

    TransactionController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

	@GetMapping("/new")
	public String newTransaction(Authentication auth, Model model) {
		User user = userService.getByEmail(auth.getName());

		categoryService.ensureDefaults(user);
		model.addAttribute("categories", categoryService.activeFor(user));

		if (!model.containsAttribute("transactionForm")) {
			TransactionForm form = new TransactionForm();
			form.setDate(LocalDate.now());
			model.addAttribute("transactionForm", form);
		}

		return "transaction-form";
	}

	@PostMapping
	public String create(Authentication auth, @Valid @ModelAttribute("transactionForm") TransactionForm form,
			BindingResult binding, RedirectAttributes ra) {
		if (binding.hasErrors()) {
			ra.addFlashAttribute("org.springframework.validation.BindingResult.transactionForm", binding);
			ra.addFlashAttribute("transactionForm", form);
			return "redirect:/transactions/new";
		}

		User user = userService.getByEmail(auth.getName());
		transactionService.create(user, form);

		ra.addFlashAttribute("success", "Expense added.");
		return "redirect:/dashboard";
	}

	@GetMapping("/{id}/edit")
	public String edit(Authentication auth,
			@PathVariable Long id,
			Model model) {
		User user = userService.getByEmail(auth.getName());

		Transaction exp = transactionService.findByIdForUser(id, user)
				.orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

		categoryService.ensureDefaults(user);
		model.addAttribute("categories", categoryService.activeFor(user));

		TransactionForm form = new TransactionForm();
		form.setDate(exp.getDate());
		form.setAmount(exp.getAmount());
		form.setDescription(exp.getDescription());
		form.setType(exp.getType());
		if (exp.getCategory() != null) {
			form.setCategoryId(exp.getCategory().getId());
		}

		model.addAttribute("transactionForm", form);
		model.addAttribute("transactionId", id);
		model.addAttribute("formTitle", "Edit transaction");
		model.addAttribute("submitLabel", "Update");
		return "transaction-form";
	}

	@PostMapping("/{id}")
	public String update(Authentication auth, @PathVariable Long id, @Valid @ModelAttribute("transactionForm") TransactionForm form,
			BindingResult binding, Model model, RedirectAttributes ra) {
		if (binding.hasErrors()) {
			User user = userService.getByEmail(auth.getName());
			categoryService.ensureDefaults(user);
			model.addAttribute("categories", categoryService.activeFor(user));
			model.addAttribute("transactionId", id);
			model.addAttribute("formTitle", "Edit Transaction");
			model.addAttribute("submitLabel", "Update");
			return "transaction-form";
		}

		User user = userService.getByEmail(auth.getName());
		transactionService.update(user, id, form);

		ra.addFlashAttribute("success", "Transaction updated.");
		return "redirect:/dashboard";
	}

	@PostMapping("/{id}/delete")
	public String delete(Authentication auth, @PathVariable Long id, RedirectAttributes ra) {
		User user = userService.getByEmail(auth.getName());
		transactionService.delete(user, id);
		ra.addFlashAttribute("success", "Transaction deleted.");
		return "redirect:/dashboard";
	}


}

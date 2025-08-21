package com.serhat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.serhat.entity.User;
import com.serhat.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String homeDirect(Authentication auth) {
    	boolean loggedIn = auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
        return loggedIn ? "redirect:/dashboard" : "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "success", required = false) String success, @RequestParam(value = "error", required = false) String error, Model model) {
        if (success != null) {
            model.addAttribute("successMessage", "Account created successfully. Please log in.");
        }

        if (error != null) {
        	model.addAttribute("errorMessage", "Invalid email or password.");
        }

        return "login";

    }

    @GetMapping("/register")
    public String homePage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {

    	if (user.getEmail() != null) {
    		user.setEmail(user.getEmail().trim().toLowerCase());
    	}

    	// Bean validation error
        if (result.hasErrors()) {
            user.setRawPassword(null);
            user.setConfirmPassword(null);
            return "register";
        }

        // Password match new user
        if (user.getRawPassword() == null || !user.getRawPassword().equals(user.getConfirmPassword())) {
        	result.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
        	user.setRawPassword(null);
        	user.setConfirmPassword(null);
        	return "register";
        }

        // Email uniqueness check before attempting save
        if (userService.emailExists(user.getEmail())) {
        	result.rejectValue("email", "email.exists" ,"Email already in use");
        	user.setRawPassword(null);
        	user.setConfirmPassword(null);
        	return "register";
        }

        // Failing to save user
        if (!userService.registerUser(user)) {
        	model.addAttribute("user", user);
        	model.addAttribute("saveError", "Failed to save user. Try again.");
        	user.setRawPassword(null);
        	user.setConfirmPassword(null);
        	return "register";
        }

        return "redirect:/login?success";
    }

    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard";
    }
}

package com.serhat.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.serhat.entity.User;
import com.serhat.repository.CategoryRepository;
import com.serhat.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired private UserRepository repo;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private CategoryRepository categoryRepo;

    private static final List<String> DEFAULTS = List.of(
			"Housing", "Utilities","Food","Transportation","Insurance",
			"Healthcare","Entertainment","Education","Travel","Gifts","Other");

	public boolean registerUser(User user) {

		try {
			if (user.getEmail() != null) {
				user.setEmail(user.getEmail().trim().toLowerCase());
			}

			user.setPassword(passwordEncoder.encode(user.getRawPassword()));
			repo.save(user);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean emailExists(String email) {
		return repo.findByEmail(email).isPresent();
	}

	public User getByEmail(String email) {
		return repo.findByEmail(email)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
	}

	private void seedDefaultCategories(User user) {
		for (String name : DEFAULTS) {
			if (!categoryRepo.existsByUserAndNameIgnoreCase(user, name)) {
				categoryRepo.save(
						com.serhat.entity.Category.builder()
							.user(user)
							.name(name)
							.active(true)
							.build());
			}
		}
	}

}

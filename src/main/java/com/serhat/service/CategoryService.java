package com.serhat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.serhat.entity.Category;
import com.serhat.entity.User;
import com.serhat.repository.CategoryRepository;

@Service
public class CategoryService {

	@Autowired private CategoryRepository categoryRepo;


	private static final List<String> DEFAULTS = List.of(
			"Housing","Utilities","Food","Transportation","Insurance",
		    "Healthcare","Entertainment","Education","Travel","Gifts","Other"
		    );

	@Transactional
	public void ensureDefaults(User user) {
		if (categoryRepo.findByUserOrderByNameAsc(user).isEmpty()) {
			List<Category> toSave = DEFAULTS.stream()
					.filter(n -> !categoryRepo.existsByUserAndNameIgnoreCase(user, n))
					.map(n -> Category.builder().user(user).name(n).active(true).build())
					.toList();
			if (!toSave.isEmpty()) categoryRepo.saveAll(toSave);
		}
	}

	public List<Category> activeFor(User user) {
		return categoryRepo.findByUserAndActiveTrueOrderByNameAsc(user);
	}
}

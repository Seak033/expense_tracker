package com.serhat.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "expenses")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder

public class Expense {

	@Id
	@SequenceGenerator(name = "expense_seq", sequenceName = "expense_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expense_seq")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="category_id", nullable = false)
	private Category category;			// reference to a category

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal amount;

	@Column(length=256)
	private String description;

	@Column(nullable = false)
	private LocalDate date;

}

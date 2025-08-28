package com.serhat.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "transactions")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder

public class Transaction {

	@Id
	@SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name="user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name="category_id", nullable = true)
	private Category category;			// reference to a category

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal amount;

	@Size(max = 20, message = "Description must be at most 20 characters")
	@Column(length=20)
	private String description;

	@Column(nullable = false)
	private LocalDate date;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private TransactionType type;

}

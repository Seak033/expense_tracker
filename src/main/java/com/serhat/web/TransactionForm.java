package com.serhat.web;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.serhat.entity.TransactionType;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
public class TransactionForm {
	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate date;

	@NotNull @DecimalMin("0.01")
	private BigDecimal amount;

	private Long categoryId;

	@Size(max = 20)
	private String description;

	@NotNull(message = "What is it? Choose one!")
	private TransactionType type;

	@AssertTrue(message = "Category is required for expenses")
	public boolean isCategoryValidForExpense() {
		return type != TransactionType.EXPENSE || categoryId != null;
	}
}

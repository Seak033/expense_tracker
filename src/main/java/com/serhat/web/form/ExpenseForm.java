package com.serhat.web.form;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
public class ExpenseForm {
	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate date;

	@NotNull @DecimalMin("0.01")
	private BigDecimal amount;

	@NotNull
	private Long categoryId;

	@Size(max = 256)
	private String description;
}

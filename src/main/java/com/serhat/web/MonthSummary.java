package com.serhat.web;

import java.math.BigDecimal;
import lombok.*;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor @Builder
public class MonthSummary {
	private int month;
	private BigDecimal income;
	private BigDecimal expense;
}

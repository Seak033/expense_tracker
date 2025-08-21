package com.serhat.entity;

import jakarta.persistence.*;
import lombok.*;

// categories
// Housing, Utilities, Food, Transportation, Insurance, HealthCare, Entertainment, Education, Travel, Gifts, Other

@Entity
@Table(
		name = "categories",
		uniqueConstraints = @UniqueConstraint(
				name = "uk_category_user_name",
				columnNames = {"user_id", "name"}))
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @Builder
@ToString(exclude = "user")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category {

	@Id
	@SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
	@EqualsAndHashCode.Include
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;  	// Owner of this category

	@Column(nullable = false, length = 64)
	private String name;			// Category names like "Food", "Utilities"

	@Column(nullable = false)
	private boolean active = true;		// true = usable, false = disabled
}
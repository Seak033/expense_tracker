package com.serhat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

	@Id
	@SequenceGenerator(
			name = "user_seq",
			sequenceName = "user_seq")
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "user_seq")
	private Integer id;

	@Column(length = 128, nullable = false, unique = true)
	@NotEmpty(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;

	@Transient
	@NotBlank(message = "Password is required")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
			message = "Password must contain at least one alphabetical character, one digit, one special character, and be at least 8 characters long.")
	private String rawPassword;

	private String password;

	@Transient
	@NotBlank(message = "Confirm Password is required")
	private String confirmPassword;

	@Column(name = "first_name", length = 64, nullable = false)
	@Size(min = 2, message = "First Name must be at least 2 characters long")
	@NotBlank(message = "First Name is required")
	@Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "First Name should start with a capital and contain only alphabets.")
	private String firstName;

	@Column(name = "last_name", length = 45, nullable = false)
	@Size(min = 2, message = "Length must be greater than 1")
	@NotBlank(message = "Last Name is required")
	@Pattern(regexp = "^[A-Z][a-zA-Z]*$", message = "Last Name should start with a capital letter and contain only alphabets.")
    private String lastName;

}

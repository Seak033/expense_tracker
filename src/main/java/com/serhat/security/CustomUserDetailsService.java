package com.serhat.security;

import com.serhat.entity.User;
import com.serhat.repository.UserRepository;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

    public CustomUserDetailsService(@Lazy UserRepository userRepository) {
        this.userRepository = userRepository;
    }


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		String normalizedEmail = (email == null) ? "" : email.trim().toLowerCase();
		User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + normalizedEmail));

		return new org.springframework.security.core.userdetails.User(
				user.getEmail(),
				user.getPassword(),
				Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
		);
	}

}

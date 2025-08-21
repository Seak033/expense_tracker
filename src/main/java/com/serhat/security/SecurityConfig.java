package com.serhat.security;


import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

	 private final CustomUserDetailsService customUserDetailsService;

	 public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
		 this.customUserDetailsService = customUserDetailsService;
	    }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((authorize) -> authorize
					.requestMatchers("/register", "/save", "/login", "/css/**", "/js/**").permitAll()
					.anyRequest().authenticated()
			)
			.formLogin((form) -> form
					.loginPage("/login")
					.usernameParameter("email")
					.passwordParameter("password")
					.defaultSuccessUrl("/dashboard", true)
					.failureUrl("/login?error")
					.permitAll()
			)
			.logout((logout) -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login?logout")
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID")
			)

			.csrf(csrf -> csrf.disable());

		return http.build();
	}


	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	 @Bean
	 public FilterRegistrationBean<NoCacheFilter> noCacheFilter() {
		 FilterRegistrationBean<NoCacheFilter> registrationBean = new FilterRegistrationBean<>();

		 registrationBean.setFilter(new NoCacheFilter());
		 registrationBean.addUrlPatterns("/*");
		 registrationBean.setOrder(1);

		 return registrationBean;
	 }
}

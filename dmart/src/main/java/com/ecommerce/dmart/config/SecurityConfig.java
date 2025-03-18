package com.ecommerce.dmart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ecommerce.dmart.service.UserAuthServiceImpl;

@Configuration
public class SecurityConfig {
	
	private final JwtAuthFilter jwtAuthFilter;
	private final UserAuthServiceImpl userDetailsService;
	  
	public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserAuthServiceImpl userDetailsService) {
		super();
		this.jwtAuthFilter = jwtAuthFilter;
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception
	{
		http.csrf(csrf -> csrf.disable()) // Disable CSRF
			.authorizeHttpRequests(
			auth -> auth.requestMatchers("/auth/**").permitAll()
			.requestMatchers("/users/register").permitAll() 
			.anyRequest().authenticated()
			)
			.securityContext(securityContext -> securityContext
		            .requireExplicitSave(false)) // Use default security context behavior
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
		
	}

		@Bean
	    public AuthenticationManager authenticationManager() {
	        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	        provider.setUserDetailsService(userDetailsService);
	        provider.setPasswordEncoder(passwordEncoder());
	        return new ProviderManager(provider);
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

}

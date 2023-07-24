package com.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	// First way to write the configuration's option
	/*
	 * @Bean 
	 * public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception { 
	 * return httpSecurity 
	 * .authorizeHttpRequests()
	 * .requestMatchers("v1/index2").permitAll() 
	 * .anyRequest().authenticated()
	 * .and() 
	 * .formLogin().permitAll() 
	 * .and() 
	 * .build(); }
	 */
	
	// Second way to write the configuration's option
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.authorizeHttpRequests(auth -> {
					auth.requestMatchers("/v1/index2").permitAll();
					auth.anyRequest().authenticated();
				})
				.formLogin(auth -> {
					auth.successHandler(succesHandler());
					auth.permitAll();
				})				
				.sessionManagement(auth -> {
					auth.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
					auth.invalidSessionUrl("/login");
					auth.maximumSessions(1)
						.sessionRegistry(sessionRegistry())
						.expiredUrl("/login");
					auth.sessionFixation()
						.migrateSession();
				})
				.build();
	}
	
	public AuthenticationSuccessHandler succesHandler() {
		return ((request, response, authentication) -> {
			response.sendRedirect("/v1/session");
		});
	}
	
	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}
}

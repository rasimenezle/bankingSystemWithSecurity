package com.rasime.bankingSystemwithMyBatish.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rasime.bankingSystemwithMyBatish.jwt.AuthorizationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private AuthorizationFilter authorizationFilter;
	private UserDetailsServiceImpl userService;

	@Autowired
	public WebSecurityConfig(UserDetailsServiceImpl userService, AuthorizationFilter authorizationFilter) {
		this.userService = userService;
		this.authorizationFilter = authorizationFilter;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors().and().csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.authorizeRequests().antMatchers("/banking/auth").permitAll();
		http.authorizeRequests().antMatchers("/banking/accounts/***").hasAuthority("CREATE_ACCOUNT");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/banking/accounts").hasAuthority("REMOVE_ACCOUNT");
		http.authorizeRequests().antMatchers("/banking/accounts").hasAuthority("CREATE_ACCOUNT");
		http.authorizeRequests().anyRequest().authenticated();
		http.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
		http.formLogin().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(this.userService);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
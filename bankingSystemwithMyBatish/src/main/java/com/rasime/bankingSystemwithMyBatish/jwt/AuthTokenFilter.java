package com.rasime.bankingSystemwithMyBatish.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.rasime.bankingSystemwithMyBatish.dtoRequest.LoginRequestDto;
import com.rasime.bankingSystemwithMyBatish.dtoResponse.LoginDtoResponse;
import com.rasime.bankingSystemwithMyBatish.security.UserDetailsServiceImpl;
@Component
public class AuthTokenFilter {

	private JwtUtils jwtUtil;
	private UserDetailsServiceImpl userService;
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthTokenFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtil,
			UserDetailsServiceImpl userService) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}

	public LoginDtoResponse login(LoginRequestDto loginRequestDto) {
		LoginDtoResponse loginDtoResponse = new LoginDtoResponse();
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getUserName(),
					loginRequestDto.getPassword()));

			final UserDetails userDetails = this.userService.loadUserByUsername(loginRequestDto.getUserName());
			final String token = jwtUtil.generateToken(userDetails);

			loginDtoResponse.setStatus("success");
			loginDtoResponse.setToken(token);

		} catch (BadCredentialsException e) {
			loginDtoResponse = null;
		} catch (DisabledException e) {
		}
		return loginDtoResponse;
	}
}
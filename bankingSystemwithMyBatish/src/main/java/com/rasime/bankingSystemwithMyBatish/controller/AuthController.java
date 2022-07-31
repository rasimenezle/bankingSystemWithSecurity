package com.rasime.bankingSystemwithMyBatish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasime.bankingSystemwithMyBatish.dtoRequest.LoginRequestDto;
import com.rasime.bankingSystemwithMyBatish.dtoResponse.LoginDtoResponse;
import com.rasime.bankingSystemwithMyBatish.jwt.AuthTokenFilter;

@RestController
@RequestMapping(path = "/banking")
public class AuthController {

	private AuthTokenFilter authTokenFilter;

	@Autowired
	public AuthController(AuthTokenFilter authenticationFilter) {
		this.authTokenFilter = authenticationFilter;
	}

	@PostMapping("/auth")
	public ResponseEntity<LoginDtoResponse> login(@RequestBody @Validated LoginRequestDto request) {
		LoginDtoResponse dto = this.authTokenFilter.login(request);
		System.err.println(dto);
		if (dto == null) {

			return new ResponseEntity<LoginDtoResponse>(new LoginDtoResponse("Bad Credentials", null),
					HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok().body(dto);
	}

}

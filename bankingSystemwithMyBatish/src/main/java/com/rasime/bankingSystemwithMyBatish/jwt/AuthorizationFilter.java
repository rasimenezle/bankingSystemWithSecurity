package com.rasime.bankingSystemwithMyBatish.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rasime.bankingSystemwithMyBatish.security.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {
	
	private UserDetailsServiceImpl userService;

	private JwtUtils jwtUtil;
	
	
	@Autowired
	public AuthorizationFilter(UserDetailsServiceImpl userService, JwtUtils jwtUtil) {
		
		this.userService = userService;
		this.jwtUtil = jwtUtil;
	}



	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				username = jwtUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {

			} catch (ExpiredJwtException e) {

			} catch (Exception e) {

			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}
		if (username != null) {

			UserDetails userDetails = this.userService.loadUserByUsername(username);
			if (jwtUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}
}
package com.rasime.bankingSystemwithMyBatish.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rasime.bankingSystemwithMyBatish.entity.User;
import com.rasime.bankingSystemwithMyBatish.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	UserService userService;
	
	
	@Autowired
	public UserDetailsServiceImpl(UserService userService) {
		super();
		this.userService = userService;
	}



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findByUsername(username);
		if(user==null) {
			throw new UsernameNotFoundException(username);
		}
		String[] authoritiesList = user.getAuthorities().split(",");

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String auth : authoritiesList) {
			authorities.add(new SimpleGrantedAuthority(auth));
		}
		return new CostumUser(user.getId(),user.getUsername(),user.getPassword(),authorities);
	}
}
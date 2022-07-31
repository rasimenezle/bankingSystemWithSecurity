package com.rasime.bankingSystemwithMyBatish.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
public class CostumUser extends User {

	
	private static final long serialVersionUID = 1L;
	private int id;
	public CostumUser(int id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, true, true, true, true, authorities);

		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
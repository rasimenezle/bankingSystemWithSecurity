package com.rasime.bankingSystemwithMyBatish.service;

import com.rasime.bankingSystemwithMyBatish.entity.User;

public interface UserService {
	User findByUsername(String username);

}

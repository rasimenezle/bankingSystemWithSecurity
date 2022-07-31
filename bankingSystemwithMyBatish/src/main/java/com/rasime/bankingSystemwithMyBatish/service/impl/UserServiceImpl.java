package com.rasime.bankingSystemwithMyBatish.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rasime.bankingSystemwithMyBatish.entity.User;
import com.rasime.bankingSystemwithMyBatish.repository.UserRepository;
import com.rasime.bankingSystemwithMyBatish.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	
	UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}

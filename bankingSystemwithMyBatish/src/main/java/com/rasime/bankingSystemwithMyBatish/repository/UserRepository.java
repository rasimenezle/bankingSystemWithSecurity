package com.rasime.bankingSystemwithMyBatish.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.rasime.bankingSystemwithMyBatish.entity.User;

@Mapper
public interface UserRepository {
	
	@Select("SELECT * FROM user WHERE userName=#{userName} ")
	 public User findByUsername(String userName);

}

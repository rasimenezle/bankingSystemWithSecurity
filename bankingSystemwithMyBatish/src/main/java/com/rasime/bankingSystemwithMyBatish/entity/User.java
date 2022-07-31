package com.rasime.bankingSystemwithMyBatish.entity;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Alias("User")
public class User {
	  
	  private int id;
	 
	  private String username;
	  
	  private String authorities;
	 
	  private String password;
	  
	}
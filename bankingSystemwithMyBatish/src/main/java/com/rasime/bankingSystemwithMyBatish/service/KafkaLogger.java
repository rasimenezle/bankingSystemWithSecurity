package com.rasime.bankingSystemwithMyBatish.service;

import java.util.List;

import com.rasime.bankingSystemwithMyBatish.entity.LogEntity;

public interface KafkaLogger {

	public String getDetail(int id);
	public List<LogEntity> getAccountIdLog(int id);
	public List<LogEntity> getDetailLog();
}

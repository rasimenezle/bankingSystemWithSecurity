package com.rasime.bankingSystemwithMyBatish.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rasime.bankingSystemwithMyBatish.entity.LogEntity;
import com.rasime.bankingSystemwithMyBatish.repository.KafkaLoggerRepository;
import com.rasime.bankingSystemwithMyBatish.service.KafkaLogger;

@Service
public class KafkaLoggerImpl implements KafkaLogger {

	@Autowired
	KafkaLoggerRepository kafkaRepository;

	@Override
	public String getDetail(int id) {
		LogEntity logEntity = kafkaRepository.findLogDetailId(id);

		return jsonString(logEntity);

	}

	@Override
	public List<LogEntity> getDetailLog() {
		
		List<LogEntity> logs=kafkaRepository.findLogs();
		return logs;
	}
	
	@Override
	public List<LogEntity> getAccountIdLog(int id) {
		List<LogEntity> logs=kafkaRepository.findLogDetailByAccountId(id);
		return logs;
	}

	// logFormat method
	public String jsonString(LogEntity logEntity) {

		String message;
		if (logEntity.getTransferedAccount() == 0) {

			message = "{" + "\n\rlog:" + logEntity.getAccountId() + " nolu hesaba " + logEntity.getBalance() + " : "
					+ logEntity.getType() + " yatırılmıştır" + "}";

		} else {
			message = "{" + "\n\rlog:" + logEntity.getAccountId() + " nolu hesabtan "
					+ logEntity.getTransferedAccount() + " nolu hesaba " + logEntity.getBalance() + " "
					+ logEntity.getType() + " yatırılmıştır" + "}";

		}
		return message;

	}

	


}

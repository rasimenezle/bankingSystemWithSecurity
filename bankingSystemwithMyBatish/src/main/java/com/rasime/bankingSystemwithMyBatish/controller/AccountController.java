package com.rasime.bankingSystemwithMyBatish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rasime.bankingSystemwithMyBatish.dtoRequest.CreateAccountDtoRequest;
import com.rasime.bankingSystemwithMyBatish.dtoRequest.TransferAccountDtoRequest;
import com.rasime.bankingSystemwithMyBatish.dtoRequest.UpdateAccountDtoRequest;
import com.rasime.bankingSystemwithMyBatish.dtoResponse.AccountDetailDtoResponse;
import com.rasime.bankingSystemwithMyBatish.dtoResponse.CreateAccountDtoResponse;
import com.rasime.bankingSystemwithMyBatish.dtoResponse.GenericAccountDtoResponse;
import com.rasime.bankingSystemwithMyBatish.entity.Account;
import com.rasime.bankingSystemwithMyBatish.entity.AccountType;
import com.rasime.bankingSystemwithMyBatish.security.CostumUser;
import com.rasime.bankingSystemwithMyBatish.service.AccountCrudService;
import com.rasime.bankingSystemwithMyBatish.service.KafkaLogger;

@CrossOrigin(origins = "http://localhost:9090")
@RestController
@RequestMapping(path = "/banking")
public class AccountController {

	@Autowired
	AccountCrudService accountCrudService;
	@Autowired
	KafkaLogger logService;

	// Insert accountController
	@PostMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> createAccount(@RequestBody CreateAccountDtoRequest accountCreateRequest) {

		boolean typeControl = accountCreateRequest.getType().equals(AccountType.ONS.toString())
				|| accountCreateRequest.getType().equals(AccountType.USD.toString())
				|| accountCreateRequest.getType().equals(AccountType.TRY.toString());

		if (typeControl) {

			int accountNumber = accountCrudService.saveAccount(accountCreateRequest);

			return new ResponseEntity<>(
					CreateAccountDtoResponse.builder().message("Account Created").accountNumber(accountNumber).build(),
					HttpStatus.OK);

		} else {

			return new ResponseEntity<>(
					CreateAccountDtoResponse.builder()
							.message("Invalid Account Type" + accountCreateRequest.getType().toString()).build(),
					HttpStatus.BAD_REQUEST);
		}

	}

	// Detail account Controller
	@GetMapping(path = "/accounts/{id}")
	public ResponseEntity<?> detailAccount(@PathVariable(name = "id") int id) {

		
		//authorizaton check
		int userAccountId = accountCrudService.findById(id).getUserId();
		CostumUser user = (CostumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.getId() != userAccountId) {
			return new ResponseEntity<>(GenericAccountDtoResponse.builder().message("Invalid Account Number").build(),
					HttpStatus.FORBIDDEN);
		}

		Account account = accountCrudService.findById(id);
		long last = account.getLastModified();

		if (id == account.getId()) {

			return ResponseEntity.ok().lastModified(last).body(account);

		} else {

			return new ResponseEntity<>(AccountDetailDtoResponse.builder().message("Don't created Account").build(),
					HttpStatus.NOT_FOUND);
		}

	}

	// Update account
	@PatchMapping(path = "/accounts/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> updateAccountBalance(@PathVariable(name = "id") int id,
			@RequestBody UpdateAccountDtoRequest updateBalanceRequest) {
		
		//authorizaton check
		int userAccountId = accountCrudService.findById(id).getUserId();
		CostumUser user = (CostumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.getId() == userAccountId) {
			return new ResponseEntity<>(GenericAccountDtoResponse.builder().message("Invalid Account Number").build(),
					HttpStatus.FORBIDDEN);
		}
		
		Account account = accountCrudService.findById(id);

		if (account != null) {

			accountCrudService.updateBalanceWithAccountId(id, updateBalanceRequest);
			Account accountUpdate = accountCrudService.findById(id);
			return ResponseEntity.ok().body(accountUpdate);
		}

		else {
			return new ResponseEntity<>(GenericAccountDtoResponse.builder().message("Invalid Account Number").build(),
					HttpStatus.BAD_REQUEST);
		}
	}

	// Transfered amount account controller
	@PatchMapping(path = "/accounts/transfer/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity<?> transferBalance(@PathVariable(name = "id") int id,
			@RequestBody TransferAccountDtoRequest transferBalanceRequest) {
		//authorizaton check
		int userAccountId = accountCrudService.findById(id).getUserId();
		CostumUser user = (CostumUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.getId() == userAccountId) {
			return new ResponseEntity<>(GenericAccountDtoResponse.builder().message("Invalid Account Number").build(),
					HttpStatus.FORBIDDEN);
		}
		return ResponseEntity.ok(accountCrudService.transferAmount(id, transferBalanceRequest));

	}

	// Delete account Controller
	@DeleteMapping("/accounts/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		//authorizaton check
		accountCrudService.deleteById(id);
		Account account = accountCrudService.findById(id);
		if (account.isDeleteAccount() == true) {
			return new ResponseEntity<>(GenericAccountDtoResponse.builder().message("Deleted acoount").build(),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(GenericAccountDtoResponse.builder().message("Dont delete").build(),
					HttpStatus.BAD_REQUEST);

		}

	}

	// Log Detail with log id
	@GetMapping(path = "/log/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> logDetail(@PathVariable(name = "id") int id) {

		return ResponseEntity.ok().body(logService.getDetail(id));

	}

	// Log Detail with all logs
	@GetMapping(path = "/logs", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> logDetail() {

		return ResponseEntity.ok().body(logService.getDetailLog());

	}

	// log Detail with account id
	@GetMapping(path = "/logsAccount/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> logDetailAccountId(@PathVariable(name = "accountId") int accountId) {

		return ResponseEntity.ok().body(logService.getAccountIdLog(accountId));

	}

}

package com.bank.management.system.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

//import javax.security.auth.login.AccountNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.management.system.exception.InsufficientBalanceException;
import com.bank.management.system.exception.AccountNotFoundException;
import com.bank.management.system.model.Account;
import com.bank.management.system.model.AccountHistory;
import com.bank.management.system.request.CreateAccountRequest;
import com.bank.management.system.request.DateRangeRequest;
import com.bank.management.system.request.DepositRequest;
import com.bank.management.system.request.WithdrawalRequest;
import com.bank.management.system.response.AccountResponse;
import com.bank.management.system.service.AccountService;

@RestController
@RequestMapping("/BMS")
public class AccountController {

    @Autowired
    private AccountService accountService;
    
    private final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @PostMapping("/create-account")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest request) {
        Integer employeeId = request.getEmployeeId();

        // Logging
        logger.info("Received request to create account for employeeId: " + employeeId);

        // Check if the employeeId is provided
        if (employeeId == null) {
            AccountResponse response = new AccountResponse();
            response.setStatus("03");
            response.setMessage("Employee ID cannot be null");
            logger.error("Employee ID is null. Cannot create account.");
            return ResponseEntity.badRequest().body(response);
        }

        // Check if the employee already has an account
        if (accountService.employeeHasAccount(employeeId)) {
            AccountResponse response = new AccountResponse();
            response.setStatus("01");
            response.setMessage("Account already exists.");
//            response.setAccountNumber(accountService.getAccountNumberByEmployeeId(employeeId));
            logger.info("Account already exists for employeeId: " + employeeId);
            return ResponseEntity.ok(response);
        }

        try {
            Account createdAccount = accountService.createAccount(request);
            if (createdAccount != null) {
                AccountResponse response = new AccountResponse();
                response.setStatus("00");
                response.setMessage("Account created successfully!");
                response.setAccountNumber(createdAccount.getAccountNumber());
                logger.info("Account created successfully for employeeId: " + employeeId);
                return ResponseEntity.ok(response);
            } else {
                AccountResponse response = new AccountResponse();
                response.setStatus("04");
                response.setMessage("Failed to create account.");
                logger.error("Failed to create account for employeeId: " + employeeId);
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            AccountResponse response = new AccountResponse();
            response.setStatus("02");
            response.setMessage(e.getMessage());
            logger.error("An error occurred while creating account for employeeId: " + employeeId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> depositAmount(@RequestBody DepositRequest depositRequest) {
        // Logging
        logger.info("Received request to deposit amount for employeeId: " + depositRequest.getEmployeeId());

        Double amount = depositRequest.getAmount();

        // Check if the amount is negative
        if (amount < 0) {
            logger.error("Invalid deposit amount. Amount cannot be negative.");
            return ResponseEntity.badRequest().body("Invalid deposit amount. Amount cannot be negative.");
        }

        String result = accountService.depositAmount(depositRequest.getEmployeeId(), depositRequest.getAmount());
        if (result.startsWith("Deposit successful")) {
            logger.info("Amount deposited successfully for employeeId: " + depositRequest.getEmployeeId());
            return ResponseEntity.ok(result);
        } else {
            logger.error("Failed to deposit amount for employeeId: " + depositRequest.getEmployeeId());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawMoney(@RequestBody WithdrawalRequest request) {
        // Logging
        logger.info("Received request to withdraw amount for employeeId: " + request.getEmployeeId());

        try {
            Double withdrawalAmount = request.getWithdrawalAmount();

            // Check if the withdrawal amount is negative or zero
            if (withdrawalAmount < 0) {
                logger.error("Invalid withdrawal amount. Amount cannot be negative.");
                return ResponseEntity.badRequest().body("Invalid withdrawal amount. Amount cannot be negative.");
            }

            String result = accountService.withdrawMoney(request.getEmployeeId(), withdrawalAmount);
            logger.info("Amount withdrawn successfully for employeeId: " + request.getEmployeeId());
            return ResponseEntity.ok(result);
        } catch (AccountNotFoundException e) {
            logger.error("Account not found for employeeId: " + request.getEmployeeId(), e);
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (InsufficientBalanceException e) {
            logger.error("Insufficient balance for employeeId: " + request.getEmployeeId(), e);
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Invalid withdrawal amount for employeeId: " + request.getEmployeeId(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/accountHistory/{accountNumber}")
    public ResponseEntity<List<AccountHistory>> getAccountHistory(@PathVariable String accountNumber) {
        // Logging
        logger.info("Received request to get account history for account number: " + accountNumber);

        List<AccountHistory> accountHistory = accountService.getAccountHistory(accountNumber);
        logger.info("Retrieved account history for account number: " + accountNumber);
        return ResponseEntity.ok(accountHistory);
    }

    @GetMapping("/accountHistory/all")
    public ResponseEntity<List<AccountHistory>> getAllAccountHistory() {
        // Logging
        logger.info("Received request to get all account history.");

        List<AccountHistory> allAccountHistory = accountService.getAllAccountHistory();
        logger.info("Retrieved all account history.");
        return ResponseEntity.ok(allAccountHistory);
    }

    @PostMapping("/transactions/{accountNumber}")
    public ResponseEntity<?> getAccountHistoryBetweenDates(
            @PathVariable String accountNumber,
            @RequestBody DateRangeRequest dateRangeRequest) {
        
        // Logging
        logger.info("Received request to get account history for account number " + accountNumber
                + " between dates: " + dateRangeRequest.getStartDate() + " and " + dateRangeRequest.getEndDate());

        try {
            LocalDateTime startDate = dateRangeRequest.getStartDateTime();
            LocalDateTime endDate = dateRangeRequest.getEndDateTime();

            // Check if the start date and end date are valid
            if (startDate == null || endDate == null) {
                logger.error("Invalid date range provided.");

                return ResponseEntity.badRequest().body("Invalid date range provided. Make sure to provide both startDate and endDate.");
            }

            List<AccountHistory> transactions = accountService.getAccountHistoryBetweenDates(accountNumber, startDate, endDate);
            logger.info("Retrieved account history for account number " + accountNumber
                    + " between dates: " + startDate + " and " + endDate);
            return ResponseEntity.ok(transactions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
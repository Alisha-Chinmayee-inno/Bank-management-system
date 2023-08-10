package com.bank.management.system.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
//import java.util.UUID;

//import javax.security.auth.login.AccountNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.management.system.exception.AccountNotFoundException;
import com.bank.management.system.model.Account;
import com.bank.management.system.model.AccountHistory;
import com.bank.management.system.model.TransactionType;
import com.bank.management.system.repository.AccountHistoryRepository;
import com.bank.management.system.repository.AccountRepository;
import com.bank.management.system.request.CreateAccountRequest;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private AccountHistoryRepository accountHistoryRepository;
    
    
    private final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public Account createAccount(CreateAccountRequest request) {
        validateInput(request, request.getEmployeeId(), request.getAccountType());

        // Logging
        logger.info("Received request to create account for employeeId: " + request.getEmployeeId());

        // Check if the employee already has an account
        if (employeeHasAccount(request.getEmployeeId())) {
            logger.info("Account already exists for employeeId: " + request.getEmployeeId());
        	return null;
        }

        // Create an account for the employee
        Account newAccount = new Account();
        newAccount.setEmployeeId(request.getEmployeeId());
        newAccount.setAccountNumber(generateUniqueAccountNumber()); // Generate a random 11-digit numeric account number
        newAccount.setAccountType(request.getAccountType()); 
        newAccount.setBalance(0.0); // Set initial balance

        // Save the new account to the database using the AccountRepository
        Account savedAccount = accountRepository.save(newAccount);
        logger.info("Account created successfully for employeeId: " + request.getEmployeeId());

        return savedAccount;
    }

    public boolean employeeHasAccount(Integer employeeId) {
        // Logging
        logger.info("Checking if employee has an account for employeeId: " + employeeId);

        // Query the AccountRepository to check if there are any accounts associated with the given employeeId
        Optional<Account> account = accountRepository.findByEmployeeId(employeeId);

        return account.isPresent();
    }
	
    private String generateUniqueAccountNumber() {
        Random random = new Random();

        // Generate a random 11-digit numeric number
        StringBuilder accountNumber = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            int digit = random.nextInt(10); // Generates a random number between 0 and 9
            accountNumber.append(digit);
        }
        return accountNumber.toString();
    }
    	
    public String getAccountNumberByEmployeeId(Integer employeeId) {
        validateInput(employeeId);

        // Logging
        logger.info("Fetching account number for employeeId: " + employeeId);

        Optional<Account> account = accountRepository.findByEmployeeId(employeeId);
        return account.map(Account::getAccountNumber).orElse(null);
    }
    	
    public String depositAmount(Integer employeeId, Double amount) {
        validateInput(employeeId, amount);

        // Logging
        logger.info("Received request to deposit amount for employeeId: " + employeeId);

        // Check if the employee has an account
        Optional<Account> optionalAccount = accountRepository.findByEmployeeId(employeeId);
        if (optionalAccount.isEmpty()) {
            return "Account not found for Employee ID: " + employeeId;
        }

        // Get the account from Optional
        Account account = optionalAccount.get();

        if (amount < 0) {
            return "Invalid deposit amount. Amount cannot be negative.";
        }
        
        // Create AccountHistory entry for deposit
        AccountHistory accountHistory = new AccountHistory();
        TransactionType transactionType = TransactionType.DEPOSIT;
        accountHistory.setTransactionType(transactionType.toString());
        accountHistory.setCustomerAccountNumber(account.getAccountNumber()); 
        accountHistory.setEmployeeId(employeeId);
        accountHistory.setTransactionAmount(amount);  
        accountHistory.setDateOfTransaction(LocalDateTime.now());

        // Calculate and set the balance after the deposit
        Double newBalance = account.getBalance() + amount;
        accountHistory.setBalance(newBalance);

        // Save the updated account to the database
        account.setBalance(newBalance);
        accountRepository.save(account);

        // Save the AccountHistory entry for deposit to the database
        accountHistoryRepository.save(accountHistory);

        logger.info("Amount deposited successfully for employeeId: " + employeeId);
        return "Deposit successful. New balance: " + newBalance;
    }


    	 
  
    public String withdrawMoney(Integer employeeId, double withdrawalAmount) {
        validateInput(employeeId);

        // Logging
        logger.info("Received request to withdraw amount for employeeId: " + employeeId);

        // Check if the employee has an account
        Optional<Account> optionalAccount = accountRepository.findByEmployeeId(employeeId);
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException("Account not found for Employee ID: " + employeeId);
        }

        // Get the account from Optional
        Account account = optionalAccount.get();

        // Check if the withdrawal amount is valid (greater than or equal to zero)
        if (withdrawalAmount < 0) {
            throw new IllegalArgumentException("Invalid withdrawal amount. Amount cannot be negative.");
        }

        // Calculate updated balance after withdrawal
        double updatedBalance = account.getBalance() - withdrawalAmount;

        // Update account balance and last transaction details
        account.setBalance(updatedBalance);
        // account.setLastTransaction("Withdrawal of " + withdrawalAmount);
        account.setUpdatedDate(LocalDateTime.now());

        // Save the updated account details
        accountRepository.save(account);

        // Create AccountHistory entry for withdrawal
        AccountHistory accountHistory = new AccountHistory();
        TransactionType transactionType = TransactionType.WITHDRAWAL;
        accountHistory.setTransactionType(transactionType.toString());
        accountHistory.setCustomerAccountNumber(account.getAccountNumber());
        accountHistory.setEmployeeId(employeeId);
        accountHistory.setTransactionAmount(withdrawalAmount);  
        accountHistory.setDateOfTransaction(LocalDateTime.now());
        accountHistory.setBalance(updatedBalance);   

        // Save the AccountHistory entry to the database
        accountHistoryRepository.save(accountHistory);

        logger.info("Amount withdrawn successfully for employeeId: " + employeeId);
        return "Withdrawal successful. Current balance: " + updatedBalance;
    }


    // Method to fetch transaction history details for a specific account using account number
    public List<AccountHistory> getAccountHistory(String accountNumber) {
        validateInput(accountNumber);

        // Logging
        logger.info("Fetching account history for account number: " + accountNumber);

        return accountHistoryRepository.findByCustomerAccountNumber(accountNumber);
    }
    
    public List<AccountHistory> getAllAccountHistory() {
        // Logging
        logger.info("Fetching all account history.");

        return accountHistoryRepository.findAllByOrderByDateOfTransactionDesc();
    }
    
    public List<AccountHistory> getAccountHistoryBetweenDates(String accountNumber, LocalDateTime startDate, LocalDateTime endDate) {
        validateInput(startDate, endDate);

        // Logging
        logger.info("Fetching account history between dates for accountNumber: " + accountNumber
                + " - Start Date: " + startDate + ", End Date: " + endDate);

        List<AccountHistory> transactions = accountHistoryRepository.findByAccountNumberAndDateOfTransactionBetween(accountNumber, startDate, endDate);

        // Sort transactions by date in descending order (most recent first)
        transactions.sort((transaction1, transaction2) ->
                transaction2.getDateOfTransaction().compareTo(transaction1.getDateOfTransaction()));

        return transactions;
    }



    // Helper method for input validation
    private void validateInput(Object... inputs) {
        for (Object input : inputs) {
            if (input == null) {
                throw new IllegalArgumentException("Invalid input. Input cannot be null.");
            }

            if (input instanceof Integer && (Integer) input <= 0) {
                throw new IllegalArgumentException("Invalid input. Integer value must be greater than zero.");
            }

            if (input instanceof Double && (Double) input < 0) {
                throw new IllegalArgumentException("Invalid input. Double value cannot be negative.");
            }

            if (input instanceof String && ((String) input).isEmpty()) {
                throw new IllegalArgumentException("Invalid input. String value cannot be empty.");
            }
        }
    }
}
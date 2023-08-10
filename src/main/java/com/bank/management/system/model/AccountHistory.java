package com.bank.management.system.model;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AccountHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="TRANSACTION_ID")
    private UUID transactionId;

    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;

    @Column(name = "ACCOUNT_NUMBER")
    private String customerAccountNumber;

    @Column(name = "EMPLOYEE_ID")
    private Integer employeeId;
    
    @Column(name = "BALANCE")
    private Double balance;
    
    @Column(name = "TRANSACTION_AMOUNT")
    private Double transactionAmount;

    
    @Column(name = "DATE_OF_TRANSACTION")
    private LocalDateTime dateOfTransaction;
    
    

	public AccountHistory() {
		super();
	}



	public UUID getTransactionId() {
		return transactionId;
	}



	public void setTransactionId(UUID transactionId) {
		this.transactionId = transactionId;
	}



	public String getTransactionType() {
		return transactionType;
	}



	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}



	public String getCustomerAccountNumber() {
		return customerAccountNumber;
	}



	public void setCustomerAccountNumber(String customerAccountNumber) {
		this.customerAccountNumber = customerAccountNumber;
	}



	public Integer getEmployeeId() {
		return employeeId;
	}



	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}



	public Double getBalance() {
		return balance;
	}



	public void setBalance(Double balance) {
		this.balance = balance;
	}



	public Double getTransactionAmount() {
		return transactionAmount;
	}



	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}



	public LocalDateTime getDateOfTransaction() {
		return dateOfTransaction;
	}



	public void setDateOfTransaction(LocalDateTime dateOfTransaction) {
		this.dateOfTransaction = dateOfTransaction;
	}
}
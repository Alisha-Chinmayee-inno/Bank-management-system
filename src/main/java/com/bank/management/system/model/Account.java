package com.bank.management.system.model;

import java.time.LocalDateTime;
import java.util.List;

//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Account")
public class Account {

	@Id
    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;
    @NotNull
    @Column(name = "EMPLOYEE_ID")
    private Integer employeeId;

    @NotNull
    @Column(name = "ACCOUNT_TYPE")
    private String accountType;

    @Column(name = "BALANCE")
    private Double balance;

    @CreationTimestamp
    @Column(name = "CREATED_DATE", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "UPDATED_DATE")
    private LocalDateTime updatedDate;

    @Column(name = "LAST_TRANSACTION")
    private String lastTransaction;

    @Column(name = "NO_OF_TRANSACTIONS")
    private Integer NoOfTransactions;
    
    public Account() {
        super();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }


    public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getLastTransaction() {
        return lastTransaction;
    }

    public void setLastTransaction(String lastTransaction) {
        this.lastTransaction = lastTransaction;
    }

    public Integer getNoOfTransactions() {
        return NoOfTransactions;
    }

    public void setNoOfTransactions(Integer noOfTransactions) {
        NoOfTransactions = noOfTransactions;
    }

}
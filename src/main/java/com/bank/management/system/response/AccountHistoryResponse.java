package com.bank.management.system.response;

import java.time.LocalDateTime;

public class AccountHistoryResponse {
	  
	    
	    private String transactionType;

	    private String customerAccountNumber;

	    private Integer employeeId;

	    private LocalDateTime dateOfTransaction;
	    
	    private Double amount;

	   
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

		public LocalDateTime getDateOfTransaction() {
			return dateOfTransaction;
		}

		public void setDateOfTransaction(LocalDateTime dateOfTransaction) {
			this.dateOfTransaction = dateOfTransaction;
		}

		 public Double getAmount() {
		        return amount;
		    }

		    public void setAmount(Double amount) {
		        this.amount = amount;
		    }	
 
}

package com.bank.management.system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bank.management.system.model.Account;

public interface AccountRepository extends JpaRepository<Account, String>{
	
	    @Query("SELECT a FROM Account a WHERE a.employeeId = :employeeId")
	    Optional<Account> findByEmployeeId(Integer employeeId);
	  
	    
	}

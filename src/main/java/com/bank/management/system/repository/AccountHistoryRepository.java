package com.bank.management.system.repository;

import com.bank.management.system.model.AccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccountHistoryRepository extends JpaRepository<AccountHistory, String> {

    List<AccountHistory> findByCustomerAccountNumber(String accountNumber);

    List<AccountHistory> findAllByOrderByDateOfTransactionDesc();

    List<AccountHistory> findByDateOfTransactionBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT ah FROM AccountHistory ah WHERE ah.customerAccountNumber = :accountNumber AND ah.dateOfTransaction BETWEEN :startDate AND :endDate ORDER BY ah.dateOfTransaction DESC")
    List<AccountHistory> findByAccountNumberAndDateOfTransactionBetween(String accountNumber, LocalDateTime startDate, LocalDateTime endDate);
}

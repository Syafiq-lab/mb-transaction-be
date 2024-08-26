package com.maybank.transactionbackend.repository;

import com.maybank.transactionbackend.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	Page<Transaction> findByCustomerId(String customerId, Pageable pageable);

	Page<Transaction> findByAccountNumber(String accountNumber, Pageable pageable);

	Page<Transaction> findByDescriptionContaining(String description, Pageable pageable);

	Page<Transaction> findByCustomerIdContainingOrAccountNumberContainingOrDescriptionContaining(
			String customerId, String accountNumber, String description, Pageable pageable
	);

}

package com.maybank.transactionbackend.service;

import com.maybank.transactionbackend.dto.TransactionUpdateRequest;
import com.maybank.transactionbackend.exception.ResourceNotFoundException;
import com.maybank.transactionbackend.model.Transaction;
import com.maybank.transactionbackend.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransactionService {

	private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

	@Autowired
	private TransactionRepository transactionRepository;

	public Page<Transaction> searchTransactions(String customerId, String accountNumber, String description, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("trxDate").descending().and(Sort.by("trxTime").descending()));

		logger.info("Searching transactions with customerId={}, accountNumber={}, description={}", customerId, accountNumber, description);

		if (customerId != null) {
			return transactionRepository.findByCustomerId(customerId, pageable);
		} else if (accountNumber != null) {
			return transactionRepository.findByAccountNumber(accountNumber, pageable);
		} else if (description != null) {
			return transactionRepository.findByDescriptionContaining(description, pageable);
		} else {
			return transactionRepository.findAll(pageable);
		}
	}

	@Transactional
	public Transaction updateTransaction(Long id, TransactionUpdateRequest request) throws Exception {
		logger.info("Updating transaction with id={}", id);
		Optional<Transaction> optionalTransaction = transactionRepository.findById(id);
		if (optionalTransaction.isPresent()) {
			Transaction transaction = optionalTransaction.get();
			if (!transaction.getVersion().equals(request.getVersion())) {
				throw new Exception("Transaction has been updated by another process.");
			}
			transaction.setDescription(request.getDescription());
			logger.info("Transaction with id={} updated successfully", id);
			return transactionRepository.save(transaction);
		} else {
			throw new ResourceNotFoundException("Transaction not found with id=" + id);
		}
	}
}

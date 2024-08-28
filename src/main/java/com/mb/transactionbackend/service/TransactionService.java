package com.mb.transactionbackend.service;

import com.mb.transactionbackend.dto.TransactionUpdateRequest;
import com.mb.transactionbackend.exception.ResourceNotFoundException;
import com.mb.transactionbackend.model.Transaction;
import com.mb.transactionbackend.repository.TransactionRepository;
import jakarta.persistence.PersistenceException;
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

		if (customerId != null && !customerId.isEmpty()) {
			return transactionRepository.findByCustomerId(customerId, pageable);
		} else if (accountNumber != null && !accountNumber.isEmpty()) {
			return transactionRepository.findByAccountNumber(accountNumber, pageable);
		} else if (description != null && !description.isEmpty()) {
			return transactionRepository.findByDescriptionContaining(description, pageable);
		} else {
			return transactionRepository.findAll(pageable);
		}
	}

	public Page<Transaction> searchTransactionsAnyField(String searchTerm, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("trxDate").descending().and(Sort.by("trxTime").descending()));

		logger.info("Searching transactions across any field with searchTerm={}", searchTerm);

		return transactionRepository.findByCustomerIdContainingOrAccountNumberContainingOrDescriptionContaining(searchTerm, searchTerm, searchTerm, pageable);
	}

	@Transactional
	public Transaction updateTransaction(Long id, TransactionUpdateRequest request) throws Exception {
		logger.info("Updating transaction with id={}", id);
		Optional<Transaction> optionalTransaction = transactionRepository.findById(id);

		if (optionalTransaction.isPresent()) {
			Transaction transaction = optionalTransaction.get();

			// Update only the fields provided in the request
			if (request.getDescription() != null) {
				transaction.setDescription(request.getDescription());
			}
			if (request.getTrxAmount() != null) {
				transaction.setTrxAmount(request.getTrxAmount());
			}
			if (request.getTrxDate() != null) {
				transaction.setTrxDate(request.getTrxDate());
			}
			if (request.getTrxTime() != null) {
				transaction.setTrxTime(request.getTrxTime());
			}

			try {
				// Save the updated entity
				Transaction updatedTransaction = transactionRepository.save(transaction);
				logger.info("Transaction with id={} updated successfully", id);
				return updatedTransaction;
			} catch (Exception ex) {
				logger.error("Error saving transaction: Unexpected error occurred for id={}", id, ex);
				throw new PersistenceException("Unexpected error occurred.", ex);
			}
		} else {
			throw new ResourceNotFoundException("Transaction not found with id=" + id);
		}
	}

}

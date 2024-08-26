package com.maybank.transactionbackend.controller;

import com.maybank.transactionbackend.dto.ApiResponse;
import com.maybank.transactionbackend.dto.TransactionUpdateRequest;
import com.maybank.transactionbackend.model.Transaction;
import com.maybank.transactionbackend.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
	private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	private TransactionService transactionService;


	@GetMapping
	public ResponseEntity<ApiResponse<Map<String, Object>>> searchTransactions(
			@RequestParam(required = false) String customerId,
			@RequestParam(required = false) String accountNumber,
			@RequestParam(required = false) String description,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		// Use the existing search method that searches by customerId, accountNumber, or description
		Page<Transaction> transactions = transactionService.searchTransactions(customerId, accountNumber, description, page, size);

		// Create a custom response structure
		Map<String, Object> response = new HashMap<>();
		response.put("transactions", transactions.getContent());
		response.put("currentPage", transactions.getNumber());
		response.put("totalItems", transactions.getTotalElements());
		response.put("totalPages", transactions.getTotalPages());

		return ResponseEntity.ok(new ApiResponse<>(true, "Transactions retrieved successfully", response));
	}


	@GetMapping("/search")
	public ResponseEntity<ApiResponse<Map<String, Object>>> searchTransactionsAnyField(
			@RequestParam(required = false) String searchTerm,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		// Use the new search method that searches across any of the fields
		Page<Transaction> transactions = transactionService.searchTransactionsAnyField(searchTerm, page, size);

		// Create a custom response structure
		Map<String, Object> response = new HashMap<>();
		response.put("transactions", transactions.getContent());
		response.put("currentPage", transactions.getNumber());
		response.put("totalItems", transactions.getTotalElements());
		response.put("totalPages", transactions.getTotalPages());

		return ResponseEntity.ok(new ApiResponse<>(true, "Transactions retrieved successfully", response));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<Transaction>> updateTransaction(
			@PathVariable Long id,
			@RequestBody TransactionUpdateRequest request) {
		try {
			Transaction updatedTransaction = transactionService.updateTransaction(id, request);

			return ResponseEntity.ok(new ApiResponse<>(true, "Transaction updated successfully", updatedTransaction));
		} catch (Exception e) {
			logger.error("Error while updating transaction with id={}", id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Transaction update failed", null));
		}
	}

}
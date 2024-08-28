package com.mb.transactionbackend;

import com.mb.transactionbackend.dto.TransactionUpdateRequest;
import com.mb.transactionbackend.exception.ResourceNotFoundException;
import com.mb.transactionbackend.model.Transaction;
import com.mb.transactionbackend.repository.TransactionRepository;
import com.mb.transactionbackend.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionService transactionService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void searchTransactions_shouldReturnResults() {
		// Given
		String customerId = "12345";
		Pageable pageable = PageRequest.of(0, 10, Sort.by("trxDate").descending().and(Sort.by("trxTime").descending()));
		Transaction transaction = new Transaction();
		Page<Transaction> transactionPage = new PageImpl<>(Collections.singletonList(transaction));

		when(transactionRepository.findByCustomerId(customerId, pageable)).thenReturn(transactionPage);

		Page<Transaction> result = transactionService.searchTransactions(customerId, null, null, 0, 10);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		verify(transactionRepository, times(1)).findByCustomerId(customerId, pageable);
	}

	@Test
	void updateTransaction_whenTransactionExists_shouldUpdateTransaction() throws Exception {

		Long id = 1L;
		TransactionUpdateRequest request = new TransactionUpdateRequest();
		request.setDescription("Updated description");

		Transaction transaction = new Transaction();
		transaction.setId(id);
		transaction.setDescription("Old description");

		when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));
		when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

		Transaction updatedTransaction = transactionService.updateTransaction(id, request);

		assertNotNull(updatedTransaction);
		assertEquals("Updated description", updatedTransaction.getDescription());
		verify(transactionRepository, times(1)).findById(id);
		verify(transactionRepository, times(1)).save(transaction);
	}

	@Test
	void updateTransaction_whenTransactionDoesNotExist_shouldThrowResourceNotFoundException() {

		Long id = 1L;
		TransactionUpdateRequest request = new TransactionUpdateRequest();

		when(transactionRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> transactionService.updateTransaction(id, request));
		verify(transactionRepository, times(1)).findById(id);
		verify(transactionRepository, never()).save(any(Transaction.class));
	}
}

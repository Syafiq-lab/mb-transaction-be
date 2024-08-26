package com.maybank.transactionbackend.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TransactionUpdateRequest {
	private String accountNumber;
	private String description;
	private Double trxAmount;       // Include this field if you want to allow updating the transaction amount
	private LocalDate trxDate;         // Include this field if you want to allow updating the transaction date (format: yyyy-MM-dd)
	private LocalTime trxTime;         // Include this field if you want to allow updating the transaction time (format: HH:mm:ss)
}

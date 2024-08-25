package com.maybank.transactionbackend.dto;

import lombok.Data;

@Data
public class TransactionUpdateRequest {
	private String description;
	private Integer version;
}

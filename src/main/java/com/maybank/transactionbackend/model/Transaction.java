package com.maybank.transactionbackend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "transaction_record")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String accountNumber;
	private Double trxAmount;
	private String description;
	private LocalDate trxDate;
	private LocalTime trxTime;
	private String customerId;

	@Version
	private Integer version;

}

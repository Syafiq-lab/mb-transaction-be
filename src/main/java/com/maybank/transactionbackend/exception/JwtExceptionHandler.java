package com.maybank.transactionbackend.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JwtExceptionHandler {

	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<String> handleSignatureException(SignatureException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT signature.");
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT token has expired.");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleOtherExceptions(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
	}
}

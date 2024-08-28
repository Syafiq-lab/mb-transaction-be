package com.mb.transactionbackend.exception;

import com.mb.transactionbackend.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex, WebRequest request) {
		logger.error("Global exception caught: {}", ex.getMessage(), ex);
		ApiResponse<Object> response = new ApiResponse<>(false, "An unexpected error occurred", null);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(
			ResourceNotFoundException ex, WebRequest request) {
		logger.error("Resource not found: {}", ex.getMessage(), ex);
		ApiResponse<Object> response = new ApiResponse<>(false, ex.getMessage(), null);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiResponse<String>> handleBadCredentialsException(
			BadCredentialsException ex, WebRequest request) {
		logger.warn("Authentication failed - Bad credentials: {}", ex.getMessage());
		ApiResponse<String> response = new ApiResponse<>(false, "Invalid username or password", null);
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ApiResponse<String>> handleUsernameNotFoundException(
			UsernameNotFoundException ex, WebRequest request) {
		logger.warn("Authentication failed - Username not found: {}", ex.getMessage());
		ApiResponse<String> response = new ApiResponse<>(false, "User not found", null);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(
			AccessDeniedException ex, WebRequest request) {
		logger.warn("Access denied: {}", ex.getMessage());
		ApiResponse<String> response = new ApiResponse<>(false, "Access denied. Please login.", null);
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
			MethodArgumentNotValidException ex) {
		logger.warn("Validation failed: {}", ex.getMessage());
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		ApiResponse<Map<String, String>> response = new ApiResponse<>(false, "Validation error", errors);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}

package com.ecommerce.ordermanagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ==================== Custom Exceptions ====================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO>handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request){
    log.error("ResourceNotFoundException : {}",ex.getMessage());
    ErrorResponseDTO errorResponse= ErrorResponseDTO.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error(HttpStatus.NOT_FOUND.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .errorCode(ex.getErrorCode())
            .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDTO>HandlerDuplicateResourceException(DuplicateResourceException ex, HttpServletRequest request){
        log.error("DuplicateResourceException : {}",ex.getMessage());
        ErrorResponseDTO errorResponseDTO=ErrorResponseDTO.builder()
                .timestamp((LocalDateTime.now()))
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode(ex.getErrorCode())
                .build();
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponseDTO>handleInvalidRequestException(InvalidRequestException ex,HttpServletRequest request){
        log.error("InvalidRequestException : {}",ex.getMessage());
        ErrorResponseDTO errorResponseDTO=ErrorResponseDTO.builder()
                .timestamp((LocalDateTime.now()))
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode(ex.getErrorCode())
                .build();
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }
    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponseDTO>handleInsufficientStockException(InsufficientStockException ex,HttpServletRequest request){
        log.error("InsufficientStockException : {}",ex.getMessage());
        ErrorResponseDTO errorResponseDTO=ErrorResponseDTO.builder()
                .timestamp((LocalDateTime.now()))
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode(ex.getErrorCode())
                .build();
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }
    // ==================== Validation Exceptions ====================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<ErrorResponseDTO>handleValidationExceptions(MethodArgumentNotValidException ex,HttpServletRequest request){
        log.error("ValidationException : {}",ex.getMessage());
        //Extract field errors
        Map<String ,String> errors=new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error->{
            String fieldName= ((FieldError)error).getField();
            String errorMessage= error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });
        ErrorResponseDTO errorResponseDTO= ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .errorCode("VALIDATION_FAILED")
                .path(request.getRequestURI())
                .details(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }
    // ==================== Type Mismatch Exception ====================

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public  ResponseEntity<ErrorResponseDTO>handlerTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request){
       log.error("TypeMismatchException : {}",ex.getMessage());
       String message= String.format(
                "Invalid value '%s' for parameter '%s' expected type '%s' ",
                ex.getValue(),
                ex.getName(),
                ex.getRequiredType()!=null?ex.getRequiredType().getSimpleName():"UNKNOWN"
        );
        ErrorResponseDTO errorResponseDTO= ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(message)
                .errorCode("INVALID_PARAMETER_TYPE")
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDTO);
    }
    // ==================== Generic Exceptions ====================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO>handleIllegalArgumentException( IllegalArgumentException ex,HttpServletRequest request){
        log.error("IllegalArgumentException {} ",ex.getMessage());

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .errorCode("INVALID_ARGUMENT")
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unexpected error occurred", ex);

        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("An unexpected error occurred. Please try again later.")
                .path(request.getRequestURI())
                .errorCode("INTERNAL_ERROR")
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}

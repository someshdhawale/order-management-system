package com.ecommerce.ordermanagement.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private  LocalDateTime timestamp;
    private int status; //HTTP status code, Client can check this for error type
    private String error; //HTTP status text"Bad Request", "Not Found", "Internal Server Error"
    private String message; //Detailed error description User-friendly message
    private String path;  //Request path that caused error
    private String errorCode; //Machine-readable error identifier Examples: "PRODUCT_NOT_FOUND", "VALIDATION_FAILED"Allows client to handle errors programmatically
    private Map<String, String> details; //For validation errors: field → error message mapping
}

package com.ecommerce.ordermanagement.exception;

public class DuplicateResourceException extends  BaseException{
    public  DuplicateResourceException(String message){
        super(message,"DUPLICATE_RESOURCE");
    }

    public  DuplicateResourceException(String resourceName, String fieldName, Object fieldValue){
        super(String.format("%s with %s: %s already exists", resourceName, fieldName, fieldValue), "DUPLICATE_RESOURCE");
    }
}

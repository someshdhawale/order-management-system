package com.ecommerce.ordermanagement.exception;

import lombok.Generated;
import lombok.Getter;

@Getter
public  abstract   class BaseException extends  RuntimeException{
    private final String errorCode;
    public BaseException(String message, String errorCode){
        super(message);
        this.errorCode=errorCode;
    }
    public BaseException(String message, String errorCode, Throwable cause){
        super(message);
        this.errorCode=errorCode;
    }



}

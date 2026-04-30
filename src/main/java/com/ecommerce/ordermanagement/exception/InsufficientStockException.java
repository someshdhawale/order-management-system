package com.ecommerce.ordermanagement.exception;

public class InsufficientStockException  extends  BaseException{
    public  InsufficientStockException(String message){
        super(message,"INSUFFICIENT_STOCK");
    }
    public  InsufficientStockException(Long Id , int request,int available){
        super(
                String.format("Product with id %d has insufficient stock. Requested %d, available %d",Id,request,available),
                "INSUFFICIENT_STOCK"
        );
    }
}

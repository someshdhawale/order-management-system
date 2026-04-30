package com.ecommerce.ordermanagement.exception;

public class ResourceNotFoundException  extends  BaseException{

    public ResourceNotFoundException(String message){
        super(message,"RESOURCE NOT FOUND");
    }

    public  ResourceNotFoundException(String resourceName,Long id){
        super(String.format("%s not found with ID %s", resourceName,id),"RESOURCE NOT FOUND");
    }
    public  ResourceNotFoundException(String resourceName,String fieldName, Object fieldValue){
        super(String.format("%s not found with %s: %s ",resourceName,fieldName,fieldValue),"RESOURCE NOT FOUND");
    }

}

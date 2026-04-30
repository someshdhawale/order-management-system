package com.ecommerce.ordermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse <T>{ //Generic type :can store any type of object
    private List<T> content; //current page item
    private  int pageNumber; //current page number
    private  int pageSize; //page size
    private  long totalElements; //total number of elements
    private  int totalPages; //total number of pages
    private  boolean last; //is last page
    private  boolean first; //is first page
    private  boolean empty; // page is empty
}

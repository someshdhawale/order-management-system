package com.ecommerce.ordermanagement.service;
import com.ecommerce.ordermanagement.dto.CreateProductRequestDTO;
import com.ecommerce.ordermanagement.dto.UpdateProductRequestDTO;
import com.ecommerce.ordermanagement.dto.ProductResponseDTO;
import com.ecommerce.ordermanagement.dto.ProductSummaryDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

   //CREATE
    ProductResponseDTO createProduct(CreateProductRequestDTO createProductRequest);
    //READ
    ProductResponseDTO getProductById(Long id);
    List<ProductSummaryDTO>getAllProduct();
    List<ProductSummaryDTO>getProductsByCategory(String category);
    List<ProductSummaryDTO>getProductByName(String name);
    List<ProductSummaryDTO>getProductByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    List<ProductSummaryDTO>getActiveProducts();

    //UPDATE
    ProductResponseDTO updateProduct(Long id, UpdateProductRequestDTO updateProductRequest);
    ProductResponseDTO updateProductQuantity(Long id, Integer newQuantity);
    ProductResponseDTO updateProductActiveStatus(Long id, Boolean newStatus);
    ProductResponseDTO updateProductPrice(Long id, BigDecimal newPrice);
    //DELETE
    void deleteProduct(Long id);

    //LOGIC
    Boolean isSkuAvailable(String sku);
    long countProductByCategory(String category);


}

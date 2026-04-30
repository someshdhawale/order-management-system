package com.ecommerce.ordermanagement.repository;

import com.ecommerce.ordermanagement.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //custom query methods

    Optional<Product>findByName(String name);

    Optional<Product>findBySku(String sku);

    List<Product>findByCategory(String category);

    // Find products with price less than
    List<Product> findByPriceLessThan(BigDecimal price);

    // Find products with price between range
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Find active products
    List<Product> findByActiveTrue();

    // Find products by category and active status
    List<Product> findByCategoryAndActiveTrue(String category);

    // Find products by name containing (like search)
    List<Product> findByNameContainingIgnoreCase(String keyword);

    Page<Product> findByCategory(String category, Pageable pageable);

    //custom paginated methods:
    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findByCategoryAndActiveTrue(String category, Pageable pageable);
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

}

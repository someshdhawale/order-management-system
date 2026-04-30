package com.ecommerce.ordermanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    @SequenceGenerator(
            name="product_sequence",
            sequenceName = "product_sequence",
            initialValue = 1,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long ProdId;
    @Column(name = "name", nullable = false,length = 255)
    private String name;
    @Column(name = "description",length = 255)
    private String description;
    @Column(name="price",nullable = false,precision = 10, scale = 2)
    private BigDecimal price;
    @Column(name="quantity",nullable = false)
    private Integer quantity;
    @Column(name="category",length = 50)
    private String category;
    @Column(name="sku",length = 50,unique = true)
    private String sku;
    @Column(name="active")
    private Boolean active=true;
    @Column(name="created_date",updatable = false)
    @CreationTimestamp
    private LocalDate createdDate;
    @Column(name="updated_date")
    @UpdateTimestamp
    private LocalDate updatedDate;
}



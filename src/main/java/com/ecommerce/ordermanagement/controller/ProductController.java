package com.ecommerce.ordermanagement.controller;

import com.ecommerce.ordermanagement.dto.*;
import com.ecommerce.ordermanagement.service.ProductService;
import com.ecommerce.ordermanagement.service.ProductServiceImplementation;
import jakarta.persistence.Entity;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    //CREATE REQUEST
    @PostMapping("/create")
    public ResponseEntity<ProductResponseDTO>createProduct(@Valid @RequestBody CreateProductRequestDTO createProductRequestDTO){
        log.info("REST request to create product: {}", createProductRequestDTO.getName());
        ProductResponseDTO createProduct= productService.createProduct(createProductRequestDTO);
        log.info("Product created successfully with ID: {}", createProduct.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createProduct);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<ProductResponseDTO>getProductById(@PathVariable Long Id){
        log.info("REST request to get product with ID {}",Id);
       ProductResponseDTO getProduct=  productService.getProductById(Id);
       return ResponseEntity.ok(getProduct);
    }
    @GetMapping("/getAll")
    public  ResponseEntity<List<ProductSummaryDTO>>getAllProducts(){
        log.info("Fetching all products");
        List<ProductSummaryDTO>getAllProducts=productService.getAllProduct();
        return  ResponseEntity.ok(getAllProducts);
    }
    @GetMapping("/search")
    public ResponseEntity<List<ProductSummaryDTO>>searchProduct(@RequestParam(required = false) String name){
        log.info("REST request to get product by name {}",name);
        List<ProductSummaryDTO>product= productService.getProductByName(name);
        return ResponseEntity.ok(product);
    }
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductSummaryDTO>>getProductByRange(@RequestParam(required = false, defaultValue = "0")BigDecimal minPrice,
                                                                    @RequestParam(required = false,defaultValue = "100") BigDecimal maxPrice){
    log.info("REST request to get Product by price range {} - {}",minPrice,maxPrice);
    List<ProductSummaryDTO>product= productService.getProductByPriceRange(minPrice,maxPrice);
    return ResponseEntity.ok(product);
    }
    @GetMapping("/active")
    public ResponseEntity<List<ProductSummaryDTO>>getActiveProducts(){
        log.info("REST request to get all active products");
        List<ProductSummaryDTO>activeProducts=productService.getActiveProducts();
        return ResponseEntity.ok(activeProducts);
    }
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductSummaryDTO>>searchByCategory(@PathVariable String category){
        log.info("REST request to get product by category {}",category);
        List<ProductSummaryDTO>product= productService.getProductsByCategory(category);
        return ResponseEntity.ok(product);

    }
    //UPDATE REQUEST
    @PutMapping("/{Id}")
    public ResponseEntity<ProductResponseDTO>updateProduct(@PathVariable Long Id ,@Valid @RequestBody UpdateProductRequestDTO update){
    log.info("REST request to update the product with ID{} ",Id);
        ProductResponseDTO updatedProduct= productService.updateProduct(Id,update);
        return ResponseEntity.ok(updatedProduct);
    }
    @PatchMapping("/{Id}/quantity")
    public ResponseEntity<ProductResponseDTO> UpdateQuantity(@PathVariable Long Id ,@RequestParam Integer quantity){
        log.info("REST request to update the product stock");
        ProductResponseDTO updateStock= productService.updateProductQuantity(Id,quantity);
        return ResponseEntity.ok(updateStock);
    }

    @PatchMapping("/{Id}/update-status")
    public  ResponseEntity<ProductResponseDTO>updateStatus( @PathVariable Long Id, @RequestParam Boolean status){
        ProductResponseDTO updateStatus= productService.updateProductActiveStatus(Id, status);
        return  ResponseEntity.ok(updateStatus);
    }
    @PatchMapping("/update-price")
    public ResponseEntity<ProductResponseDTO>updatePrice(@RequestParam(required = true) Long Id , @RequestParam(required = false) BigDecimal newPrice){
        log.info("REST request to update price to {}", newPrice);
        ProductResponseDTO updatePrice=productService.updateProductPrice(Id,newPrice);
        return ResponseEntity.ok(updatePrice);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        log.info("REST request to delete product with ID {} " ,id);
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/check-sku")
    public ResponseEntity<Boolean>checkSku(@RequestParam(required = false) String sku){
        log.info("REST request to check SKU {}",sku);
        boolean isAvailable= productService.isSkuAvailable(sku);
        return ResponseEntity.ok(isAvailable);
    }
    @GetMapping("/get-count")
    public  ResponseEntity<Long>getCount(@RequestParam(required = false) String category){
        log.info("REST request to get count of product by category{}",category);
        long count=productService.countProductByCategory(category);
        return ResponseEntity.ok(count);
    }
    // ==================== PAGINATED ENDPOINTS ====================
    @GetMapping("/paginated/getAll")
    public  ResponseEntity<PageResponse<ProductSummaryDTO>>getAllProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ProdId") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection){
        log.info("REST request to get paginated products : Page {},size {}, sortBy {} sortDirection {}",page,size,sortBy,sortDirection);
        PageResponse<ProductSummaryDTO>pageResponse=productService.getAllProductsPaginated(page,size,sortBy,sortDirection);
        return ResponseEntity.ok(pageResponse);
    }
    @GetMapping("/paginated/{category}")
    public ResponseEntity<PageResponse<ProductSummaryDTO>>getProductByCategoryPaginated( @PathVariable String category, @RequestParam(defaultValue = "0") int page, @RequestParam("10") int size){
    log.info("REST request to get paginated products by category {} : Page {},size {}",category,page,size);
        PageResponse<ProductSummaryDTO>pageResponse=productService.getProductsByCategoryPaginated(category,page,size);
    return ResponseEntity.ok(pageResponse);
    }
    @GetMapping("/paginated/search")
    public ResponseEntity<PageResponse<ProductSummaryDTO>>searchPaginated(@RequestParam String search, @RequestParam (defaultValue = "0") int page,@RequestParam("10") int size){
        log.info("REST request to search paginated product by {} ",search);
        PageResponse<ProductSummaryDTO>pageResponse=productService.searchProductsPaginated(search,page,size);
        return ResponseEntity.ok(pageResponse);
    }
    @GetMapping("/paginated/price-range")
    public ResponseEntity<PageResponse<ProductSummaryDTO>>getProductsByPriceRangePaginated(@RequestParam BigDecimal minPrice, @RequestParam BigDecimal maxPrice, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        log.info("REST request to get paginated products by price range {} - {} : Page {},size {}",minPrice,maxPrice,page,size);
        PageResponse<ProductSummaryDTO>pageResponse=productService.getProductsByPriceRangePaginated(minPrice,maxPrice,page,size);
        return ResponseEntity.ok(pageResponse);
    }
    @GetMapping("/paginated/active")
    public ResponseEntity<PageResponse<ProductSummaryDTO>>getActiveProductsPaginated(@RequestParam(required = false) String active, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        log.info("REST request to get paginated products by active status {} : Page {},size {}",active,page,size);
        PageResponse<ProductSummaryDTO>pageResponse=productService.getActiveProductsPaginated(active,page,size);
        return ResponseEntity.ok(pageResponse);
    }








}

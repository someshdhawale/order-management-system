package com.ecommerce.ordermanagement.service;

import com.ecommerce.ordermanagement.dto.CreateProductRequestDTO;
import com.ecommerce.ordermanagement.dto.ProductResponseDTO;
import com.ecommerce.ordermanagement.dto.ProductSummaryDTO;
import com.ecommerce.ordermanagement.dto.UpdateProductRequestDTO;
import com.ecommerce.ordermanagement.exception.DuplicateResourceException;
import com.ecommerce.ordermanagement.exception.InvalidRequestException;
import com.ecommerce.ordermanagement.exception.ResourceNotFoundException;
import com.ecommerce.ordermanagement.model.Product;
import com.ecommerce.ordermanagement.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProductServiceImplementation.class);

    public ProductServiceImplementation(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponseDTO createProduct(CreateProductRequestDTO createProductRequest) {
        log.info("Creating new product with SKU: {}", createProductRequest.getSku());
        //check if product with same sku exists
        if(productRepository.findBySku(createProductRequest.getSku()).isPresent()){
            log.warn("Product with SKU {} already exists", createProductRequest.getSku());
            throw new DuplicateResourceException("Product ","SKU ",createProductRequest.getSku());
        }
        // Convert DTO to Entity
        Product product = Product.builder()
                .name(createProductRequest.getName())
                .description(createProductRequest.getDescription())
                .price(createProductRequest.getPrice())
                .quantity(createProductRequest.getQuantity())
                .sku(createProductRequest.getSku().toUpperCase())
                .category(createProductRequest.getCategory())
                .active(true)
                .build();
    Product savedProduct= productRepository.save(product);
    log.info("Product created with id: {}", savedProduct.getProdId());
        ProductResponseDTO response= new ProductResponseDTO(
                savedProduct.getProdId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getPrice(),
                savedProduct.getQuantity(),
                savedProduct.getSku(),
                savedProduct.getCategory(),
                savedProduct.getActive(),
                savedProduct.getCreatedDate(),
                savedProduct.getUpdatedDate());

          return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
       /* log.info("Fetching product with ID {}",id);
        if(productRepository.findById(id).isEmpty()){
            log.error("Product not found with id {}", id);
            throw new IllegalArgumentException("Product not found with id " + id);
        }*/
        Product product = productRepository.findById(id)
                .orElseThrow(()->{
                    log.warn("Product not found with ID{} ",id);
                  return new ResourceNotFoundException("Product", id);
                });
         return convertToProductResponse(product);
    }
   @Override
   @Transactional(readOnly= true)
    public List<ProductSummaryDTO>getAllProduct() {
       log.info("Fetching all products");
        return productRepository.findAll().stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional(readOnly = true)
    public  List<ProductSummaryDTO>getProductByName (String name){
        log.info("Fetching products by name{}",name);
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToSummaryDTO)
                .toList();
    }
    @Override
    @Transactional(readOnly = true)
    public List<ProductSummaryDTO>getProductByPriceRange(BigDecimal minPrice, BigDecimal maxPrice){
        log.info("Fetching products with price between {} and {}",minPrice,maxPrice);
        return productRepository.findByPriceBetween(minPrice,maxPrice).stream()
                .map(this::convertToSummaryDTO)
                .toList();
    }

    @Transactional(readOnly=true)
    @Override
    public List<ProductSummaryDTO> getActiveProducts(){
        log.info("Fetching active products");
        return productRepository.findByActiveTrue().stream()
                .map(this::convertToSummaryDTO)
                .toList();
    }



    @Transactional(readOnly=true)
    @Override
    public List<ProductSummaryDTO>getProductsByCategory(String category){
        log.info("Fetching products by category{}",category);
        return productRepository.findByCategory(category).stream()
                .map(this::convertToSummaryDTO)
                .toList();
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, UpdateProductRequestDTO updateProductRequest) {
        log.info("Updating product with ID{}",id);
        Product product=productRepository.findById(id)
                .orElseThrow(()->{
                    log.warn("Product not found with ID {} ",id);
                    return new ResourceNotFoundException("Product ", id);
                });
        if(updateProductRequest.getName()!=null){
            product.setName(updateProductRequest.getName());}
        if (updateProductRequest.getDescription()!=null){
            product.setDescription(updateProductRequest.getDescription());
        }
        if(updateProductRequest.getPrice()!=null){
            product.setPrice(updateProductRequest.getPrice());
        }
        if (updateProductRequest.getQuantity() != null) {
            product.setQuantity(updateProductRequest.getQuantity());
        }
        if (updateProductRequest.getCategory() != null) {
            product.setCategory(updateProductRequest.getCategory());
        }
        if (updateProductRequest.getActive() != null) {
            product.setActive(updateProductRequest.getActive());
        }
        Product updatedProduct=productRepository.save(product);
        return convertToProductResponse(updatedProduct);

    }

    @Override
    public ProductResponseDTO updateProductQuantity(Long id, Integer newQuantity) {
       log.info("Updating product quantity with ID{} to quantity {}",id,newQuantity);
        Product product= productRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Product ", id));
       if(newQuantity<0){
           throw new InvalidRequestException("Quantity cannot be negative");
       }
       product.setQuantity(newQuantity);
       Product updatedProduct=productRepository.save(product);

       return convertToProductResponse(updatedProduct);
    }

    @Override
    public ProductResponseDTO updateProductActiveStatus(Long id, Boolean newStatus) {
        log.info("Updating status for product ID {}",id);
        Product product= productRepository.findById(id).orElseThrow(()->  new ResourceNotFoundException("Product ",id));
        product.setActive(newStatus);
        Product updatedProduct=productRepository.save(product);

        return convertToProductResponse(updatedProduct);


    }

    @Override
    public ProductResponseDTO updateProductPrice(Long id, BigDecimal newPrice) {

      log.info("Updating price for product ID {}",id);
       Product product= productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product " , id));

        if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRequestException("Price cannot be negative");
        }
        
        product.setPrice(newPrice);
        Product updatedProduct = productRepository.save(product);
        
        return convertToProductResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
    log.info("Deleting product with ID {}",id);
    if(!productRepository.existsById(id)){
        throw new ResourceNotFoundException("Product " , id);
    }
    productRepository.deleteById(id);
    log.info("Product deleted  successfully with ID {}",id);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean isSkuAvailable(String sku) {
        log.info("Checking availability of SKU {}",sku);
        return productRepository.findBySku(sku) .isEmpty();
    }

    @Override
    public long countProductByCategory(String category) {
        log.info("Counting product with category {}",category);

        return productRepository.findByCategory(category).size();
    }

    //Helper Method to convert Product to ProductResponseDTO
    private ProductResponseDTO convertToProductResponse(Product product){
        return ProductResponseDTO.builder()
                .id(product.getProdId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .sku(product.getSku())
                .category(product.getCategory())
                .active(product.getActive())
                .createdDate(product.getCreatedDate())
                .updatedDate(product.getUpdatedDate())
                .build();
    }
    private ProductSummaryDTO convertToSummaryDTO(Product product) {
        return ProductSummaryDTO.builder()
                .id(product.getProdId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory())
                .active(product.getActive())
                .build();
    }




}


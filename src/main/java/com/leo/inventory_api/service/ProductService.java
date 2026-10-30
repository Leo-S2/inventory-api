package com.leo.inventory_api.service;

import com.leo.inventory_api.dto.CreateProductRequest;
import com.leo.inventory_api.dto.ProductResponse;
import com.leo.inventory_api.exception.ProductDuplicateNameException;
import com.leo.inventory_api.exception.ProductNotFoundException;
import com.leo.inventory_api.model.Product;
import com.leo.inventory_api.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional
    public ProductResponse addProduct(CreateProductRequest request) {
        if(productRepository.existsByName(request.name())){
            throw new ProductDuplicateNameException(request.name());
        }

        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setStock(request.stock());

        return ProductResponse.from(productRepository.save(product));
    }

    public ProductResponse findById(Long id) {
        return ProductResponse.from(getProductOrThrow(id));
    }

    private Product getProductOrThrow(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional
    public void deleteById(Long id) {
        Product product = getProductOrThrow(id);
        productRepository.delete(product);
    }

    @Transactional
    public void updateStock(Long id, int stock) {
        Product product = getProductOrThrow(id);
        product.setStock(stock);
    }

    public long count() {
        return productRepository.count();
    }

    public List<ProductResponse> getLowStock(int limit) {
        return productRepository.findByStockLessThanEqual(limit)
                .stream()
                .map(ProductResponse::from)
                .toList();
    }
}

package com.leo.inventory_api.controller;

import com.leo.inventory_api.dto.CreateProductRequest;
import com.leo.inventory_api.dto.ProductResponse;
import com.leo.inventory_api.dto.UpdateStockRequest;
import com.leo.inventory_api.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponse> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/count")
    public long count() {
        return productService.count();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.addProduct(request));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> updateProductStock(@PathVariable Long id, @Valid @RequestBody UpdateStockRequest request) {
        productService.updateStock(id, request.stock());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponse>> getLowStockProducts(@RequestParam @Min(value = 1, message = "Limit must be at least 1") int limit) {
        return ResponseEntity.ok(productService.getLowStock(limit));
    }
}

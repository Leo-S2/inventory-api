package com.leo.inventory_api.exception;

public class ProductDuplicateNameException extends RuntimeException {
    public ProductDuplicateNameException(String name) {
        super("Product with name " + name + " already exists");
    }
}

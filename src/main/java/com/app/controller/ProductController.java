package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.exceptions.ProductException;
import com.app.pojos.Product;
import com.app.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(@RequestParam String category,
    		@RequestParam(required = false) List<String> color,@RequestParam(required = false) List<String> size, @RequestParam Integer minPrice, 
            @RequestParam Integer maxPrice, @RequestParam Integer minDiscount, @RequestParam String sort, 
            @RequestParam String stock, @RequestParam Integer pageNumber, @RequestParam Integer pageSize) {

        Page<Product> res = productService.getAllProduct(category, color, size, minPrice, maxPrice, 
                minDiscount, sort, stock, pageNumber, pageSize);
        
        System.out.println("complete products");
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }
    
    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException {
        Product product = productService.findProductById(productId);
        
        return new ResponseEntity<Product>(product, HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/new-arrivals")
    public ResponseEntity<Page<Product>> getNewArrivals(
            @RequestParam(defaultValue = "all") String segment,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "12") Integer pageSize) {

        Page<Product> res = productService.getNewArrivalsBySegment(segment, pageNumber, pageSize);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    
    
    
    
}

package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.dto.ApiResponse;
import com.app.dto.ProductRequest;
import com.app.exceptions.ProductException;
import com.app.pojos.Product;
import com.app.repository.ProductRepository;
import com.app.service.ProductService;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductRepository productRepo;
	
	@PostMapping("/")
	public ResponseEntity<Product> createProduct(@RequestBody ProductRequest req) {
	    
	    Product product = productService.createProduct(req);
	    
	    return new ResponseEntity<Product>(product, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{productId}/delete")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) throws ProductException {
	    
	    productService.deleteProduct(productId);
	    
	    ApiResponse res = new ApiResponse();
	    res.setMessage("product deleted successfully");
	    res.setStatus(true);
	    
	    return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Product>> findAllProduct() {
	    List<Product> products = productRepo.findAll();
	    return new ResponseEntity<>(products, HttpStatus.OK);
	}

	@PutMapping("/{productId}/update")
	public ResponseEntity<Product> updateProduct(@RequestBody Product req, @PathVariable Long productId) throws ProductException {
	    
	    Product product = productService.updateProduct(productId, req);
	    
	    return new ResponseEntity<Product>(product, HttpStatus.CREATED);
	}

	@PostMapping("/creates")
	public ResponseEntity<ApiResponse> createMultipleProduct(@RequestBody ProductRequest[] req) {
	    
	    for(ProductRequest product:req) {
	        productService.createProduct(product);
	    }
	    
	    ApiResponse res = new ApiResponse();
	    res.setMessage("product created successfully");
	    res.setStatus(true);
	    
	    return new ResponseEntity<>(res, HttpStatus.CREATED);
	}



}

package com.app.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.app.dto.ProductRequest;
import com.app.exceptions.ProductException;
import com.app.pojos.Product;

public interface ProductService {
	
	Product createProduct(ProductRequest request);
	String deleteProduct(Long productId) throws ProductException;
	Product updateProduct(Long productId, Product product) throws ProductException;
	Product findProductById(Long productId) throws ProductException;
	List<Product> findProductByCategory(String category);
	Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, int minPrice, int maxPrice, int minDiscount, String sort, String stock,
			int pageNo, int pageSize);

}

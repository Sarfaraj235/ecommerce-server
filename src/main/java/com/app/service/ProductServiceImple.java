package com.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.dto.ProductRequest;
import com.app.exceptions.ProductException;
import com.app.pojos.Category;
import com.app.pojos.Product;
import com.app.repository.CategoryRepository;
import com.app.repository.ProductRepository;

@Service
@Transactional
public class ProductServiceImple implements ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Override
    public Product createProduct(ProductRequest request) {

        Category topLevel = categoryRepo.findByName(request.getTopLevelCategory());

        if (topLevel == null) {
            topLevel = new Category();
            topLevel.setName(request.getTopLevelCategory());
            topLevel.setLevel(1);
            topLevel = categoryRepo.save(topLevel);
        }

        Category secondLevel =
                categoryRepo.findByNameAndParent(request.getSecondLevelCategory(), topLevel);

        if (secondLevel == null) {
            secondLevel = new Category();
            secondLevel.setName(request.getSecondLevelCategory());
            secondLevel.setParentCategory(topLevel);
            secondLevel.setLevel(2);
            secondLevel = categoryRepo.save(secondLevel);
        }

        Category thirdLevel =
                categoryRepo.findByNameAndParent(request.getThirdLevelCategory(), secondLevel);

        if (thirdLevel == null) {
            thirdLevel = new Category();
            thirdLevel.setName(request.getThirdLevelCategory());
            thirdLevel.setParentCategory(secondLevel);
            thirdLevel.setLevel(3);
            thirdLevel = categoryRepo.save(thirdLevel);
        }

        Product product = new Product();
        product.setTitle(request.getTitle());
        product.setColor(request.getColor());
        product.setDescription(request.getDescription());
        product.setDiscountedPrice(request.getDiscountedPrice());
        product.setDiscountPercent(request.getDiscountPercent());
        product.setImageUrl(request.getImageUrl());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setSizes(request.getSize());
        product.setQuantity(request.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());

        return productRepo.save(product);
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        productRepo.delete(product);
        return "Product deleted successfully";
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {

        Product existingProduct = findProductById(productId);

        if (product.getTitle() != null)
            existingProduct.setTitle(product.getTitle());

        if (product.getColor() != null)
            existingProduct.setColor(product.getColor());

        if (product.getDescription() != null)
            existingProduct.setDescription(product.getDescription());

        if (product.getPrice() != null)
            existingProduct.setPrice(product.getPrice());

        if (product.getDiscountedPrice() != null)
            existingProduct.setDiscountedPrice(product.getDiscountedPrice());

        if (product.getDiscountPercent() != null)
            existingProduct.setDiscountPercent(product.getDiscountPercent());

        if (product.getQuantity() != null)
            existingProduct.setQuantity(product.getQuantity());

        if (product.getSizes() != null && !product.getSizes().isEmpty())
            existingProduct.setSizes(product.getSizes());

        if (product.getImageUrl() != null)
            existingProduct.setImageUrl(product.getImageUrl());

        if (product.getBrand() != null)
            existingProduct.setBrand(product.getBrand());

        return productRepo.save(existingProduct);
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        return productRepo.findById(id)
                .orElseThrow(() -> new ProductException("Product not found with id : " + id));
    }

    @Override
    public List<Product> findProductByCategory(String category) {
        return productRepo.findByCategoryName(category);
    }
    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes,
                                       int minPrice, int maxPrice, int minDiscount,
                                       String sort, String stock,
                                       int pageNo, int pageSize) {

        int safePageNo = Math.max(pageNo, 0);     // if your API is 1-based, use Math.max(pageNo - 1, 0)
        int safePageSize = Math.max(pageSize, 1);
        Pageable pageable = PageRequest.of(safePageNo, safePageSize);

        List<Product> products = productRepo.filterProducts(category, minPrice, maxPrice, minDiscount, sort);

        if (colors != null && !colors.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getColor() != null &&
                            colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
                    .collect(Collectors.toList());
        }

        if (sizes != null && !sizes.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getSizes() != null && p.getSizes().stream()
                            .anyMatch(s -> s.getName() != null &&
                                    sizes.stream().anyMatch(req -> req.equalsIgnoreCase(s.getName()))))
                    .collect(Collectors.toList());
        }

        if ("in_stock".equalsIgnoreCase(stock)) {
            products = products.stream()
                    .filter(p -> p.getQuantity() != null && p.getQuantity() > 0)
                    .collect(Collectors.toList());
        } else if ("out_of_stock".equalsIgnoreCase(stock)) {
            products = products.stream()
                    .filter(p -> p.getQuantity() == null || p.getQuantity() < 1)
                    .collect(Collectors.toList());
        }

        int startIndex = (int) pageable.getOffset();
        if (startIndex >= products.size()) {
            return new PageImpl<>(List.of(), pageable, products.size());
        }

        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());
        List<Product> pageContent = products.subList(startIndex, endIndex);

        return new PageImpl<>(pageContent, pageable, products.size());
    }
}

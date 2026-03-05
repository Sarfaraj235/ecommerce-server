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

        String topName = normalizeCategory(request.getTopLevelCategory());
        String secondName = normalizeCategory(request.getSecondLevelCategory());
        String thirdName = normalizeCategory(request.getThirdLevelCategory());

        Category topLevel = categoryRepo.findByNameAndLevel(topName, 1);
        if (topLevel == null) {
            topLevel = new Category();
            topLevel.setName(topName);
            topLevel.setLevel(1);
            topLevel = categoryRepo.save(topLevel);
        }

        Category secondLevel = categoryRepo.findByNameAndParentCategoryAndLevel(secondName, topLevel, 2);
        if (secondLevel == null) {
            secondLevel = new Category();
            secondLevel.setName(secondName);
            secondLevel.setParentCategory(topLevel);
            secondLevel.setLevel(2);
            secondLevel = categoryRepo.save(secondLevel);
        }

        Category thirdLevel = categoryRepo.findByNameAndParentCategoryAndLevel(thirdName, secondLevel, 3);
        if (thirdLevel == null) {
            thirdLevel = new Category();
            thirdLevel.setName(thirdName);
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

    private String normalizeCategory(String value) {
        if (value == null) return "";
        return value.trim().toLowerCase().replace("-", "_").replaceAll("\\s+", "_");
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

        int safePageNo = Math.max(pageNo, 0);
        int safePageSize = Math.max(pageSize, 1);
        Pageable pageable = PageRequest.of(safePageNo, safePageSize);

        String effectiveCategory =
                (category == null || category.isBlank() || "all".equalsIgnoreCase(category))
                        ? null
                        : category.trim().toLowerCase();

        List<Product> products = productRepo.filterProducts(
                effectiveCategory, minPrice, maxPrice, minDiscount, sort
        );

        List<String> effectiveColors = (colors == null ? List.<String>of() : colors).stream()
                .filter(c -> c != null && !c.trim().isEmpty())
                .map(String::trim)
                .filter(c -> !"all".equalsIgnoreCase(c))
                .toList();

        List<String> effectiveSizes = (sizes == null ? List.<String>of() : sizes).stream()
                .filter(s -> s != null && !s.trim().isEmpty())
                .map(String::trim)
                .filter(s -> !"all".equalsIgnoreCase(s))
                .toList();

        if (!effectiveColors.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getColor() != null &&
                            effectiveColors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
                    .collect(Collectors.toList());
        }

        if (!effectiveSizes.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getSizes() != null && p.getSizes().stream()
                            .anyMatch(s -> s.getName() != null &&
                                    effectiveSizes.stream().anyMatch(req -> req.equalsIgnoreCase(s.getName()))))
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
    
    @Override
    public Page<Product> getNewArrivalsBySegment(String segment, int pageNo, int pageSize) {
        int safePageNo = Math.max(pageNo, 0);
        int safePageSize = Math.max(pageSize, 1);
        Pageable pageable = PageRequest.of(safePageNo, safePageSize);

        String normalizedSegment = (segment == null || segment.isBlank())
                ? "all"
                : segment.trim().toLowerCase();

        return productRepo.findNewArrivalsBySegment(normalizedSegment, pageable);
    }


}

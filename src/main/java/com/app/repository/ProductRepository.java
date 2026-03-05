package com.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.pojos.Category;
import com.app.pojos.Product;
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("""
		    SELECT p FROM Product p
		    WHERE (:category IS NULL OR p.category.name = :category)
		      AND (
		            (:minPrice IS NULL AND :maxPrice IS NULL)
		            OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)
		          )
		      AND (:minDiscount IS NULL OR p.discountPercent >= :minDiscount)
		    ORDER BY
		      CASE WHEN :sort = 'newest' THEN p.createdAt END DESC,
		      CASE WHEN :sort = 'price_low' THEN p.discountedPrice END ASC,
		      CASE WHEN :sort = 'price_high' THEN p.discountedPrice END DESC,
		      p.createdAt DESC
		    """)
		List<Product> filterProducts(
		    @Param("category") String category,
		    @Param("minPrice") Integer minPrice,
		    @Param("maxPrice") Integer maxPrice,
		    @Param("minDiscount") Integer minDiscount,
		    @Param("sort") String sort
		);


	List<Product> findByCategoryName(String category);
	
	@Query("""
		    SELECT p FROM Product p
		    WHERE (
		      :segment IS NULL OR :segment = '' OR :segment = 'all'
		      OR lower(p.category.parentCategory.parentCategory.name) = lower(:segment)
		    )
		    ORDER BY p.createdAt DESC
		""")
		Page<Product> findNewArrivalsBySegment(@Param("segment") String segment, Pageable pageable);

}

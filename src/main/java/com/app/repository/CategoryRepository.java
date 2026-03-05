package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.pojos.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Category findByName(String name);

	@Query("SELECT c FROM Category c WHERE c.name = :name AND c.parentCategory = :parent")
	Category findByNameAndParent(@Param("name") String name, @Param("parent") Category parent);

	Category findByNameAndLevel(String name, int level);
	Category findByNameAndParentCategoryAndLevel(String name, Category parentCategory, int level);
}

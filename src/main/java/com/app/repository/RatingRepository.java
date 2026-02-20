package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.pojos.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long>{
	
	List<Rating> findByProduct_Id(Long productId);
}

package com.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dto.ReviewRequest;
import com.app.exceptions.ProductException;
import com.app.pojos.Product;
import com.app.pojos.Review;
import com.app.pojos.User;
import com.app.repository.ProductRepository;
import com.app.repository.ReviewRepository;

@Service
public class ReviewServiceImple implements ReviewService{
	
	@Autowired
	private ReviewRepository reviewRepo;
	
	@Autowired
	private ProductService productService;
	

	@Override
	public Review createReview(ReviewRequest request, User user) throws ProductException {

	    Product product = productService.findProductById(request.getProductId());

	    Review review = new Review();
	    review.setUser(user);
	    review.setProduct(product);
	    review.setReview(request.getReview());
	    review.setCreatedAt(LocalDateTime.now());

	    return reviewRepo.save(review);
	}


	@Override
	public List<Review> getAllReview(Long ProductId) {
		// TODO Auto-generated method stub
		return reviewRepo.findByProduct_Id(ProductId);
	}

}

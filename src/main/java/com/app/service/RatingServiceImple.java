package com.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dto.RatingRequest;
import com.app.exceptions.ProductException;
import com.app.pojos.Product;
import com.app.pojos.Rating;
import com.app.pojos.User;
import com.app.repository.RatingRepository;

@Service
public class RatingServiceImple implements RatingService{

	@Autowired
	private RatingRepository ratingRepo;
	
	@Autowired
	private ProductService productService;
	
	@Override
	public Rating createRating(RatingRequest req, User user) throws ProductException {
	    Product product=productService.findProductById(req.getProductId());

	    Rating rating=new Rating();
	    rating.setProduct(product);
	    rating.setUser(user);
	    rating.setRating(req.getRating());
	    rating.setCreatedAt(LocalDateTime.now());

	    return ratingRepo.save(rating);
	}

	@Override
	public List<Rating> getProdutsRating(Long productId) {
		// TODO Auto-generated method stub
		 return ratingRepo.findByProduct_Id(productId);
	}

}

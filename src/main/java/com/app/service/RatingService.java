package com.app.service;

import java.util.List;

import com.app.dto.RatingRequest;
import com.app.exceptions.ProductException;
import com.app.pojos.Rating;
import com.app.pojos.User;

public interface RatingService {

	public Rating createRating(RatingRequest req, User user) throws ProductException;
	public List<Rating> getProdutsRating(Long productId);




}

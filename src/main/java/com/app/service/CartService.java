package com.app.service;

import com.app.dto.AddItemRequest;
import com.app.exceptions.ProductException;
import com.app.exceptions.UserException;
import com.app.pojos.Cart;
import com.app.pojos.User;

public interface CartService {
	
	public Cart createCart(User user);
	public String addCartItem(Long userId, AddItemRequest req) throws ProductException, UserException;
	public Cart findUserCart(Long userId) throws UserException;


}

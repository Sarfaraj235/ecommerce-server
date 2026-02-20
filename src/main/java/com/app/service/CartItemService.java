package com.app.service;

import com.app.exceptions.CartItemException;
import com.app.exceptions.UserException;
import com.app.pojos.Cart;
import com.app.pojos.CartItem;
import com.app.pojos.Product;

public interface CartItemService {
	
	public CartItem createCartItem(CartItem cartItem);
	
	public CartItem updateCartItem(Long userid, Long id, CartItem cartitem) throws CartItemException, UserException;

	public CartItem isCartItemExists(Cart cart, Product product , String size, Long userId);
	
	public void removeCartItem(Long userid, Long cartitemid) throws CartItemException, UserException;
	public CartItem findCartItemById(Long cartItemid) throws CartItemException;
	}


package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.dto.AddItemRequest;
import com.app.dto.ApiResponse;
import com.app.exceptions.ProductException;
import com.app.exceptions.UserException;
import com.app.pojos.Cart;
import com.app.pojos.User;
import com.app.service.CartService;
import com.app.service.UserService;

@RestController
@RequestMapping("/api/cart")

public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private UserService userService;

	@GetMapping("/")

	public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserException {
		User user = userService.findUserProfileByJwt(jwt);
		Cart cart = cartService.findUserCart(user.getId());

		return new ResponseEntity<>(cart, HttpStatus.OK);
	}

	@PutMapping("/add")
	// @Operation(description = "add item to cart")
	public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest req,
			@RequestHeader("Authorization") String jwt) throws UserException, ProductException {

		User user = userService.findUserProfileByJwt(jwt);

		cartService.addCartItem(user.getId(), req);

		ApiResponse res = new ApiResponse();
		res.setMessage("item added to cart");
		res.setStatus(true);

		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	

}

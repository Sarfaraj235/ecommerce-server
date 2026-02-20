package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.dto.AddItemRequest;
import com.app.exceptions.ProductException;
import com.app.exceptions.UserException;
import com.app.pojos.Cart;
import com.app.pojos.CartItem;
import com.app.pojos.Product;
import com.app.pojos.User;
import com.app.repository.CartItemRepository;
import com.app.repository.CartRepository;

@Service
public class CartServiceImple implements CartService {

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private CartItemRepository cartItemRepo;
	
	@Autowired
	private UserService userService;

	public Cart createCart(User user) {
		Cart cart = new Cart();
		cart.setUser(user);
		return cartRepo.save(cart);
	}
	@Override
	public String addCartItem(Long userId, AddItemRequest req) throws ProductException , UserException{

	    Cart cart = cartRepo.findByUserId(userId);
	    if (cart == null) {
	        cart = createCart(userService.findUserById(userId));
	    }

	    Product product = productService.findProductById(req.getProductId());

	    CartItem existingItem =
	            cartItemService.isCartItemExists(cart, product, req.getSize(), userId);
	    if (existingItem == null) {
	        CartItem cartItem = new CartItem();
	        cartItem.setProduct(product);
	        cartItem.setCart(cart);
	        cartItem.setQuantity(req.getQuantity());
	        cartItem.setUserId(userId);
	        cartItem.setSize(req.getSize());

	        cartItem.setPrice(req.getQuantity() * product.getPrice()); // MRP total
	        cartItem.setDiscountedPrice(req.getQuantity() * product.getDiscountedPrice()); // discounted total

	        cartItemService.createCartItem(cartItem);
	    } else {
	        int newQty = existingItem.getQuantity() + req.getQuantity();
	        existingItem.setQuantity(newQty);
	        existingItem.setPrice(newQty * product.getPrice());
	        existingItem.setDiscountedPrice(newQty * product.getDiscountedPrice());
	        cartItemRepo.save(existingItem);
	    }


	    return "Item added to cart";
	}

	@Override
	public Cart findUserCart(Long userId) throws UserException{
		Cart cart = cartRepo.findByUserId(userId);
		
		if (cart == null) return createCart(userService.findUserById(userId));


	    int totalPrice=0;
	    int totalDiscountedPrice=0;
	    int totalItem=0;

	    for(CartItem cartitem:cart.getCartItems()) {
	        totalPrice=totalPrice+cartitem.getPrice();
	        totalDiscountedPrice=totalDiscountedPrice+cartitem.getDiscountedPrice();
	        totalItem=totalItem+cartitem.getQuantity();
	    }

	    cart.setTotalDiscountedPrice(totalDiscountedPrice);
	    cart.setTotalItem(totalItem);
	    cart.setTotalPrice(totalPrice);
	    cart.setDiscount(totalPrice - totalDiscountedPrice); 
	    
	    return cartRepo.save(cart);
	    
	}
}

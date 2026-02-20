package com.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.exceptions.CartItemException;
import com.app.exceptions.UserException;
import com.app.pojos.Cart;
import com.app.pojos.CartItem;
import com.app.pojos.Product;
import com.app.repository.CartItemRepository;

@Service
public class CartItemServiceImple implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepo;


    @Override
    public CartItem createCartItem(CartItem cartItem) {
        int qty = cartItem.getQuantity() > 0 ? cartItem.getQuantity() : 1;
        cartItem.setQuantity(qty);
        cartItem.setPrice(cartItem.getProduct().getPrice() * qty);
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * qty);
        return cartItemRepo.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem)
            throws CartItemException, UserException {

        CartItem item = findCartItemById(id);

        if (!item.getUserId().equals(userId)) {
            throw new UserException("You are not authorized to update this cart item.");
        }

        int qty = cartItem.getQuantity();
        if (qty <= 0) {
            throw new CartItemException("Quantity must be greater than 0");
        }

        item.setQuantity(qty);
        item.setPrice(item.getProduct().getPrice() * qty);
        item.setDiscountedPrice(item.getProduct().getDiscountedPrice() * qty);

        return cartItemRepo.save(item);
    }

    @Override
    public CartItem isCartItemExists(Cart cart, Product product, String size, Long userId) {
        return cartItemRepo.isCartItemExists(cart, product, size, userId);
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        CartItem cartItem = findCartItemById(cartItemId);

        if (!cartItem.getUserId().equals(userId)) {
            throw new UserException("you can't remove another users item");
        }

        cartItemRepo.deleteById(cartItemId);
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> opt = cartItemRepo.findById(cartItemId);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new CartItemException("Cart item not found with id: " + cartItemId);
    }
}

package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.exceptions.CartItemException;
import com.app.exceptions.UserException;
import com.app.pojos.Cart;
import com.app.pojos.CartItem;
import com.app.pojos.User;
import com.app.service.CartItemService;
import com.app.service.CartService;
import com.app.service.UserService;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @PutMapping("/{cartItemId}")
    public ResponseEntity<Cart> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody CartItem req,
            @RequestHeader("Authorization") String jwt
    ) throws CartItemException, UserException {

        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.updateCartItem(user.getId(), cartItemId, req);

        // return full cart so frontend can refresh totals/items directly
        Cart cart = cartService.findUserCart(user.getId());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Cart> removeCartItem(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt
    ) throws CartItemException, UserException {

        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        // return full cart so frontend can refresh totals/items directly
        Cart cart = cartService.findUserCart(user.getId());
        return ResponseEntity.ok(cart);
    }
}

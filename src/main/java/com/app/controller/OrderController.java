package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.exceptions.OrderException;
import com.app.exceptions.UserException;
import com.app.pojos.Address;
import com.app.pojos.Order;
import com.app.pojos.User;
import com.app.service.OrderService;
import com.app.service.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;
    
    @PostMapping("/")
    public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddress, 
            @RequestHeader("Authorization") String jwt) throws UserException {
        
        User user = userService.findUserProfileByJwt(jwt);
        
        Order order = orderService.createOrder(user, shippingAddress);
        
        System.out.println("order " + order);
        
        return new ResponseEntity<Order>(order, HttpStatus.CREATED);
    }
    
    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistory(
            @RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);

        List<Order> orders = orderService.usersOrderHistory(user.getId());

        return new ResponseEntity<>(orders, HttpStatus.CREATED);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<Order> findOrderById (
        @PathVariable("Id") Long orderId,
        @RequestHeader("Authorization") String jwt) throws UserException, OrderException {

        User user = userService.findUserProfileByJwt(jwt);

        Order order = orderService.findOrderById(orderId);

        return new ResponseEntity<Order>(order, HttpStatus.ACCEPTED);
    }


    
    
    
    
}

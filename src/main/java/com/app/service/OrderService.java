package com.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.exceptions.OrderException;
import com.app.exceptions.UserException;
import com.app.pojos.Address;
import com.app.pojos.Order;
import com.app.pojos.User;


public interface OrderService {
	
	 public Order createOrder(User user, Address shippingAddress) throws UserException;

	    public Order findOrderById(Long orderId) throws OrderException;

	    public List<Order> usersOrderHistory(Long userId);

	    public Order placedOrder(Long orderId) throws OrderException;

	    public Order confirmedOrder(Long orderId) throws OrderException;

	    public Order shippedOrder(Long orderId) throws OrderException;

	    public Order deliveredOrder(Long orderId) throws OrderException;

	    public Order canceledOrder(Long orderId) throws OrderException;

	    public List<Order> getAllOrders();

	    public void deleteOrder(Long orderId) throws OrderException;

}

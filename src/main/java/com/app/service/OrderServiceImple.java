package com.app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.exceptions.OrderException;
import com.app.exceptions.UserException;
import com.app.pojos.Address;
import com.app.pojos.Cart;
import com.app.pojos.CartItem;
import com.app.pojos.Order;
import com.app.pojos.OrderItems;
import com.app.pojos.OrderStatus;
import com.app.pojos.PaymentStatus;
import com.app.pojos.User;
import com.app.repository.AddressRepository;
import com.app.repository.CartRepository;
import com.app.repository.OrderItemRepository;
import com.app.repository.OrderRepository;
import com.app.repository.UserRepository;

@Service
public class OrderServiceImple implements OrderService{
	
	@Autowired
	private CartService cartService;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderItemService orderitemService;

	@Autowired
	private OrderItemRepository orderItemRepo;

	@Autowired
	private OrderRepository orderRepo;

	
	@Override
	public Order createOrder(User user, Address shippingAddress) throws  UserException{

	    // Save address
	    shippingAddress.setUser(user);
	    
	    Address address = addressRepository.save(shippingAddress);
	    user.getAddresses().add(address);
	    userRepository.save(user);

	    // Get cart
	    Cart cart = cartService.findUserCart(user.getId());
	    List<OrderItems> orderItems = new ArrayList<>();

	    // Create order items from cart items
	    for (CartItem item : cart.getCartItems()) {
	        OrderItems orderItem = new OrderItems();
	        orderItem.setPrice(item.getPrice());
	        orderItem.setProduct(item.getProduct());
	        orderItem.setQuantity(item.getQuantity());
	        orderItem.setUserId(user.getId());

	        orderItems.add(orderItem);
	    }

	    // Create order
	    Order createdOrder = new Order();
	    createdOrder.setUser(user);
	    createdOrder.setOrderId(UUID.randomUUID().toString());
	    createdOrder.setOrderItems(orderItems);
	    createdOrder.setTotalPrice(cart.getTotalPrice());
	    createdOrder.setDiscount(cart.getDiscount());
	    createdOrder.setTotalItem(cart.getTotalItem());
	    createdOrder.setShippingAddress(address);
	    createdOrder.setOrderDate(LocalDateTime.now());
	    createdOrder.setOrderStatus(OrderStatus.PENDING);
	    createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
	    createdOrder.setCreatedAt(LocalDateTime.now());

	    // Save order
	    Order savedOrder = orderRepo.save(createdOrder);

	    // Link order items to order
	    for (OrderItems item : orderItems) {
	        item.setOrder(savedOrder);
	        orderItemRepo.save(item);
	    }

	    return savedOrder;
	}


	@Override
	public Order findOrderById(Long orderId) throws OrderException {
	    Optional<Order> opt = orderRepo.findById(orderId);
	    if(opt.isPresent()) {
	        return opt.get();
	    }
	    throw new OrderException("order not exist with id " + orderId);
	}

	@Override
	public List<Order> usersOrderHistory(Long userId) {
	    List<Order> orders = orderRepo.getUsersOrders(userId);
	    return orders;
	}

	@Override
	public Order placedOrder(Long orderId) throws OrderException {
	    Order order = findOrderById(orderId);
	    order.setOrderStatus(OrderStatus.PLACED);
	    order.getPaymentDetails().setStatus(PaymentStatus.SUCCESS);
	    return order;
	}

	@Override
	public Order confirmedOrder(Long orderId) throws OrderException {
	    Order order = findOrderById(orderId);
	    order.setOrderStatus(OrderStatus.CONFIRMED);

	    return orderRepo.save(order);
	}


	@Override
	public Order shippedOrder(Long orderid) throws OrderException {
	    Order order = findOrderById(orderid);
	    order.setOrderStatus(OrderStatus.SHIPPED);
	    return orderRepo.save(order);
	}

	@Override
	public Order deliveredOrder(Long orderid) throws OrderException {
	    Order order = findOrderById(orderid);
	    order.setOrderStatus(OrderStatus.DELIVERED);
	    return orderRepo.save(order);
	}


	@Override
	public Order canceledOrder(Long orderid) throws OrderException {
	    Order order = findOrderById(orderid);
	    order.setOrderStatus(OrderStatus.CANCELLED);
	    return orderRepo.save(order);
	}

	@Override
	public List<Order> getAllOrders() {
	    return orderRepo.findAll();
	}

	@Override
	public void deleteOrder(Long orderId) throws OrderException {
	    Order order = findOrderById(orderId);
	    orderRepo.deleteById(orderId);
	}

	

}
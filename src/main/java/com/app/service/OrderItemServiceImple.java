package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.pojos.OrderItems;
import com.app.repository.OrderItemRepository;

@Service
public class OrderItemServiceImple implements OrderItemService {

	@Autowired
	private OrderItemRepository orderItemRepo;

	@Override
	public OrderItems createOrderItem(OrderItems orderItem) {
		return orderItemRepo.save(orderItem);
	}

}

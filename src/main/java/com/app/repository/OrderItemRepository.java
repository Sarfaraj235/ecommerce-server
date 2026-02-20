package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.pojos.OrderItems;

public interface OrderItemRepository extends JpaRepository<OrderItems, Long>{

}

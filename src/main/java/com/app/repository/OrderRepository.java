package com.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.pojos.Order;
import com.app.pojos.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND (o.orderStatus = 'PLACED' OR o.orderStatus = 'CONFIRMED')")
    List<Order> getUsersOrders(@Param("userId") Long userId);

    long countByOrderStatus(OrderStatus status);

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o")
    Double sumTotalRevenue();

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0) FROM Order o WHERE o.orderDate >= :from AND o.orderDate < :to")
    Double sumRevenueBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("""
        SELECT COALESCE(SUM(o.totalPrice), 0)
        FROM Order o
        WHERE o.orderDate >= :from AND o.orderDate < :to
          AND o.orderStatus IN :statuses
    """)
    Double sumRevenueBetweenWithStatuses(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("statuses") List<OrderStatus> statuses
    );

    @Query("""
        SELECT DATE(o.orderDate), COALESCE(SUM(o.totalPrice), 0)
        FROM Order o
        WHERE o.orderDate >= :from AND o.orderDate < :to
          AND o.orderStatus IN :statuses
        GROUP BY DATE(o.orderDate)
        ORDER BY DATE(o.orderDate)
    """)
    List<Object[]> dailyRevenue(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("statuses") List<OrderStatus> statuses
    );
}

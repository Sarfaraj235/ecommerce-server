package com.app.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.app.dto.DashboardOverviewDto;
import com.app.dto.RevenuePointDto;
import com.app.pojos.OrderStatus;
import com.app.repository.OrderRepository;
import com.app.repository.ProductRepository;
import com.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@org.springframework.transaction.annotation.Transactional(readOnly = true)
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public DashboardOverviewDto getOverview() {
        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime tomorrowStart = today.plusDays(1).atStartOfDay();

        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        LocalDateTime monthStart = firstDayOfMonth.atStartOfDay();
        LocalDateTime nextMonthStart = firstDayOfMonth.plusMonths(1).atStartOfDay();

        List<OrderStatus> paidStatuses = List.of(
                OrderStatus.PLACED,
                OrderStatus.CONFIRMED,
                OrderStatus.SHIPPED,
                OrderStatus.DELIVERED
        );

        Double totalRevenue = orderRepository.sumRevenueBetweenWithStatuses(
                LocalDateTime.of(2000, 1, 1, 0, 0),
                LocalDateTime.of(2999, 1, 1, 0, 0),
                paidStatuses
        );

        Double todayRevenue = orderRepository.sumRevenueBetweenWithStatuses(todayStart, tomorrowStart, paidStatuses);
        Double monthRevenue = orderRepository.sumRevenueBetweenWithStatuses(monthStart, nextMonthStart, paidStatuses);

        Long totalOrders = orderRepository.count();
        Long totalUsers = userRepository.count();
        Long totalProducts = productRepository.count();

        Long pending = orderRepository.countByOrderStatus(OrderStatus.PENDING);
        Long delivered = orderRepository.countByOrderStatus(OrderStatus.DELIVERED);
        Long cancelled = orderRepository.countByOrderStatus(OrderStatus.CANCELLED);

        LocalDateTime last7From = today.minusDays(6).atStartOfDay();
        List<Object[]> rows = orderRepository.dailyRevenue(last7From, tomorrowStart, paidStatuses);

        Map<String, Double> dayMap = new HashMap<>();
        for (Object[] row : rows) {
            dayMap.put(String.valueOf(row[0]), ((Number) row[1]).doubleValue());
        }

        List<RevenuePointDto> series = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            String key = d.toString();
            series.add(RevenuePointDto.builder()
                    .label(key)
                    .value(dayMap.getOrDefault(key, 0.0))
                    .build());
        }

        return DashboardOverviewDto.builder()
                .totalOrders(totalOrders)
                .totalUsers(totalUsers)
                .totalProducts(totalProducts)
                .totalRevenue(totalRevenue == null ? 0.0 : totalRevenue)
                .todayRevenue(todayRevenue == null ? 0.0 : todayRevenue)
                .monthRevenue(monthRevenue == null ? 0.0 : monthRevenue)
                .pendingOrders(pending)
                .deliveredOrders(delivered)
                .cancelledOrders(cancelled)
                .revenueSeries(series)
                .build();
    }
}

package com.app.dto;
import java.util.List;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
@Builder
public class DashboardOverviewDto {
    private Long totalOrders;
    private Long totalUsers;
    private Long totalProducts;

    private Double totalRevenue;
    private Double todayRevenue;
    private Double monthRevenue;

    private Long pendingOrders;
    private Long deliveredOrders;
    private Long cancelledOrders;

    private List<RevenuePointDto> revenueSeries;
}

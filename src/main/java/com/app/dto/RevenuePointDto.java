package com.app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenuePointDto {
    private String label;   // e.g. "2026-02-23"
    private Double value;
}

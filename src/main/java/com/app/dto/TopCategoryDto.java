package com.app.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopCategoryDto {
    private String category;
    private Long orders;
}

package com.app.dto;

import java.util.HashSet;
import java.util.Set;

import com.app.pojos.Size;

import lombok.*;

@Setter
@Getter

public class ProductRequest {
	
	private String title;
	private String description;
	private int price;
	private int discountedPrice;
	private int discountPercent;
	private int quantity;
	private String brand;
	private String color;
	private Set<Size> size = new HashSet<>();
	private String imageUrl;
	private String topLevelCategory;
	private String secondLevelCategory;
	private String thirdLevelCategory;
	

}

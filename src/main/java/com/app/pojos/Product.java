package com.app.pojos;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    private String title;
    @Column(length = 1000)
    private String description;

    private Integer price;

    @Column(name = "discounted_price")
    private Integer discountedPrice;

    @Column(name = "discount_percent")
    private Integer discountPercent;

    private Integer quantity;

    private String brand;

    private String color;

    @Embedded
    @ElementCollection
    @CollectionTable(name = "product_sizes", joinColumns = @JoinColumn(name = "product_id"))
    private Set<Size> sizes = new HashSet<>();

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();
    
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
    
    @Column(name = "nums_ratings")
    private int numRatings;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}



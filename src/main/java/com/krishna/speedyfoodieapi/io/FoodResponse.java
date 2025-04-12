package com.krishna.speedyfoodieapi.io;

import com.krishna.speedyfoodieapi.enums.FoodCategory;
import com.krishna.speedyfoodieapi.enums.FoodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponse {
    private String id;
    private String name;
    private String description;
    private double price;
    private FoodCategory category;
    private String customCategory;
    private FoodType foodType;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

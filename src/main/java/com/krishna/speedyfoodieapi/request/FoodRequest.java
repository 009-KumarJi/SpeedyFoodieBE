package com.krishna.speedyfoodieapi.request;

import com.krishna.speedyfoodieapi.enums.FoodCategory;
import com.krishna.speedyfoodieapi.enums.FoodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequest {
    private String name;
    private String description;
    private double price;
    private FoodCategory category;
    private String customCategory;
    private FoodType foodType;
}
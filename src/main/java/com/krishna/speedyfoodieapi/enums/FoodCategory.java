package com.krishna.speedyfoodieapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FoodCategory {
    NORTH_INDIAN("North Indian"),
    SOUTH_INDIAN("South Indian"),
    CHINESE("Chinese"),
    ITALIAN("Italian"),
    MEXICAN("Mexican"),
    THAI("Thai"),
    CONTINENTAL("Continental/European"),
    MIDDLE_EASTERN("Middle Eastern"),
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACKS("Snacks"),
    DESSERTS("Desserts/Sweets"),
    BRUNCH("Brunch/All-Day Breakfast"),
    BIRYANI("Biryani/Rice Bowls"),
    FAST_FOOD("Fast Food"),
    HEALTHY("Healthy/Diet Options"),
    VEGAN("Vegan"),
    FUSION("Fusion/Contemporary"),
    LOCAL_FAVORITES("Local Favorites"),
    TRENDING("Trending/Popular"),
    OTHER("Other");

    private final String displayName;
}
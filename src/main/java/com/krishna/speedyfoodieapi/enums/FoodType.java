package com.krishna.speedyfoodieapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FoodType {
    VEG("Vegetarian"),
    NON_VEG("Non-Vegetarian"),
    EGG("Contains Egg");

    private final String displayName;
}
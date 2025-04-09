package com.krishna.speedyfoodieapi.repository;

import com.krishna.speedyfoodieapi.entity.FoodEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FoodRepository extends MongoRepository<FoodEntity, String> {
    FoodEntity findByName(String name);
    FoodEntity findByCategory(String category);
}

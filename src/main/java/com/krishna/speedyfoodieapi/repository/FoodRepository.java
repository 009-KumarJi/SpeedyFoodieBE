package com.krishna.speedyfoodieapi.repository;

import com.krishna.speedyfoodieapi.entity.FoodEntity;
import com.krishna.speedyfoodieapi.enums.FoodCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FoodRepository extends MongoRepository<FoodEntity, String> {
    FoodEntity findByName(String name);
    List<FoodEntity> findByCategory(FoodCategory category);
    
    @Query(value = "{}", fields = "{'customCategory': 1, '_id': 0}")
    List<String> findAllCustomCategories();
}

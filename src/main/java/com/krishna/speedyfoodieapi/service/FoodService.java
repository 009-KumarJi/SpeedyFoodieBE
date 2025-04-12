package com.krishna.speedyfoodieapi.service;

import com.krishna.speedyfoodieapi.io.FoodResponse;
import com.krishna.speedyfoodieapi.request.FoodRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {
    // Upload food image to Cloudinary with param as multipart file
    String uploadFoodImage(MultipartFile file);
    FoodResponse addFood(FoodRequest foodRequest, MultipartFile file);
    List<FoodResponse> getAllFoods();
    FoodResponse getFoodById(String id);
    boolean deleteFile(String publicId);
    void deleteFoodById(String id);
    List<String> findAllCustomCategories();
}

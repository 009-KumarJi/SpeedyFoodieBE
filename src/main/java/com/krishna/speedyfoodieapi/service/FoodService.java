package com.krishna.speedyfoodieapi.service;

import com.krishna.speedyfoodieapi.io.FoodResponse;
import com.krishna.speedyfoodieapi.request.FoodRequest;
import org.springframework.web.multipart.MultipartFile;

public interface FoodService {
    // Upload food image to Cloudinary with param as multipart file
    String uploadFoodImage(MultipartFile file);
    FoodResponse addFood(FoodRequest foodRequest, MultipartFile file);
}

package com.krishna.speedyfoodieapi.service;

import com.krishna.speedyfoodieapi.entity.FoodEntity;
import com.krishna.speedyfoodieapi.io.FoodResponse;
import com.krishna.speedyfoodieapi.repository.FoodRepository;
import com.krishna.speedyfoodieapi.request.FoodRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final Cloudinary cloudinary;
    private final FoodRepository foodRepository;

    /**
     * Uploads a food image to Cloudinary.
     *
     * @param file the image file to upload
     * @return the URL of the uploaded image
     */
    @Override
    public String uploadFoodImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        }

        // Extract file extension and create unique filename
        String filenameExtension = Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        String key = UUID.randomUUID().toString() + "_" + filenameExtension;

        try {
            // Upload to Cloudinary using the generated key as public_id
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", key,
                            "resource_type", "auto",
                            "folder", "food-items"
//                            , "transformation", ObjectUtils.asMap(
//                                    "width", 500,
//                                    "height", 500,
//                                    "crop", "limit"
//                            )
                    )
            );
            if (uploadResult == null || uploadResult.isEmpty())
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image to Cloudinary");

            return uploadResult.get("secure_url").toString();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image to Cloudinary", e);
        }
    }

    @Override
    public FoodResponse addFood(FoodRequest foodRequest, MultipartFile file) {
        FoodEntity foodEntity = convertToEntity(foodRequest);
        String imageUrl = uploadFoodImage(file);
        foodEntity.setImageUrl(imageUrl);
        foodEntity = foodRepository.save(foodEntity);
        return convertToResponse(foodEntity);
    }

    private FoodResponse convertToResponse(FoodEntity foodEntity) {
        return FoodResponse.builder()
                .id(foodEntity.getId())
                .name(foodEntity.getName())
                .description(foodEntity.getDescription())
                .price(foodEntity.getPrice())
                .category(foodEntity.getCategory())
                .imageUrl(foodEntity.getImageUrl())
                .build();
    }

    private FoodEntity convertToEntity(FoodRequest foodRequest) {
        return FoodEntity.builder()
                .name(foodRequest.getName())
                .description(foodRequest.getDescription())
                .price(foodRequest.getPrice())
                .category(foodRequest.getCategory())
                .build();
    }
}
package com.krishna.speedyfoodieapi.service;

import com.krishna.speedyfoodieapi.entity.FoodEntity;
import com.krishna.speedyfoodieapi.io.FoodResponse;
import com.krishna.speedyfoodieapi.repository.FoodRepository;
import com.krishna.speedyfoodieapi.request.FoodRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final Cloudinary cloudinary;
    private final FoodRepository foodRepository;

    @Value("${cloudinary.food.images.folder}")
    private String cloudinaryFolder;

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
        String key = UUID.randomUUID() + "_" + filenameExtension;

        try {
            // Upload to Cloudinary using the generated key as public_id
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "public_id", key,
                            "resource_type", "auto",
                            "folder", cloudinaryFolder
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

    @Override
    public List<FoodResponse> getAllFoods() {
        List<FoodEntity> foodEntities = foodRepository.findAll();

        if (!foodEntities.isEmpty()) {
            return foodEntities.stream()
                    .map(this::convertToResponse)
                    .toList();
        }

        return List.of();
    }

    @Override
    public FoodResponse getFoodById(String id) {
        FoodEntity foodEntity = foodRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));
        return convertToResponse(foodEntity);
    }

    @Override
    public boolean deleteFile(String publicId) {
        try {
            Map<?, ?> deleteResult = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            if (deleteResult.get("result").equals("ok")) {
                return true;
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete image from Cloudinary", e);
        }
        return false;
    }

    @Override
    public void deleteFoodById(String id) {
        FoodEntity foodEntity = foodRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));
        String publicId = cloudinaryFolder + "/" + foodEntity.getImageUrl().substring(foodEntity.getImageUrl().lastIndexOf('/') + 1, foodEntity.getImageUrl().lastIndexOf('.'));
        boolean deleted = deleteFile(publicId);

        if (!deleted) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete image from Cloudinary");
        }

        foodRepository.delete(foodEntity);
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
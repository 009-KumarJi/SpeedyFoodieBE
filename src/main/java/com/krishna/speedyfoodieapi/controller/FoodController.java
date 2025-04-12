package com.krishna.speedyfoodieapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishna.speedyfoodieapi.io.FoodResponse;
import com.krishna.speedyfoodieapi.request.FoodRequest;
import com.krishna.speedyfoodieapi.service.FoodService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/v1/foods")
@AllArgsConstructor
@CrossOrigin("*")
public class FoodController {

    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<FoodResponse> addFood(@RequestPart("food") String foodRequestString,
                                @RequestPart("file") MultipartFile file){
        System.out.println("Received request to add food: " + foodRequestString);
        ObjectMapper objectMapper = new ObjectMapper();
        FoodRequest request = null;
        try {
            request = objectMapper.readValue(foodRequestString, FoodRequest.class);
        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON request");
        }

        FoodResponse foodResponse = foodService.addFood(request, file);
        if (foodResponse == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add food");
        }
        return ResponseEntity.created(null).body(foodResponse);
    }

    @GetMapping
    public ResponseEntity<List<FoodResponse>> readFoods() {
        List<FoodResponse> foodResponseList = foodService.getAllFoods();
        if (foodResponseList == null || foodResponseList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No foods found");
        }
        return ResponseEntity.ok(foodResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodResponse> getFoodById(@PathVariable String id) {
        FoodResponse foodResponse = foodService.getFoodById(id);
        if (foodResponse == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found");
        }
        return ResponseEntity.ok(foodResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable String id) {
        foodService.deleteFoodById(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/custom-categories")
    public ResponseEntity<List<String>> findAllCustomCategories() {
        List<String> customCategories = foodService.findAllCustomCategories();
        if (customCategories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(customCategories);
    }
}

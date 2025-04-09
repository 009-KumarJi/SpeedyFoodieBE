package com.krishna.speedyfoodieapi.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.url}")
    private String cloudUrl;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(cloudUrl);
    }
}

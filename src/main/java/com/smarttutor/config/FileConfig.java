package com.smarttutor.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Expose the uploads folder for static file serving
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
                
        System.out.println("📁 File handler configured: /uploads/** -> file:uploads/");
    }
}

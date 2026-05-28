package com.mystore.config;

import com.mystore.model.Product;
import com.mystore.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner loadInitialData(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() == 0) {
                productRepository.save(new Product("Carrot", "Root Vegetable", "bunch", 1.99, "Crisp and sweet carrots for salads and stews."));
                productRepository.save(new Product("Broccoli", "Cruciferous", "bunch", 2.49, "Fresh broccoli with a bright green crunch."));
                productRepository.save(new Product("Spinach", "Leafy Green", "bag", 1.79, "Tender baby spinach leaves rich in iron."));
                productRepository.save(new Product("Tomato", "Vine", "kg", 2.99, "Juicy red tomatoes perfect for sauces and salads."));
                productRepository.save(new Product("Potato", "Root Vegetable", "kg", 0.99, "Versatile potatoes ideal for roasting, mashing, or frying."));
                productRepository.save(new Product("Cucumber", "Salad", "piece", 0.79, "Cool, crisp cucumbers for snacks and salads."));
                productRepository.save(new Product("Capsicum", "Pepper", "piece", 1.39, "Crunchy green capsicum with a mild, sweet flavor."));
                productRepository.save(new Product("Eggplant", "Nightshade", "piece", 1.89, "Smooth eggplant perfect for grilling and casseroles."));
                productRepository.save(new Product("Cabbage", "Leafy Green", "head", 1.29, "Fresh cabbage with firm, crunchy leaves."));
                productRepository.save(new Product("Lettuce", "Leafy Green", "head", 1.99, "Crisp lettuce for sandwiches and healthy bowls."));
            }
        };
    }
}

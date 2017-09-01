package com.service;

import com.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RestaurantInfoService {
    void saveRestaurants(List<Restaurant> restaurants);
    Page<Restaurant> findByRestaurantName(String restaurantName, Pageable pageable);
    void deleteByRestaurantName(String restaurantNames);
    void deleteAll();
}

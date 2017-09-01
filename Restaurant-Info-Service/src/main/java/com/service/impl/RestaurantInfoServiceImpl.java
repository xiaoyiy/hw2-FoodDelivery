package com.service.impl;

import com.domain.Restaurant;
import com.domain.RestaurantRepository;
import com.service.NextSequenceService;
import com.service.RestaurantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantInfoServiceImpl implements RestaurantInfoService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    NextSequenceService nextSequenceService;

    public void saveRestaurants(List<Restaurant> restaurants) {

        for(Restaurant restaurant : restaurants) {
            restaurant.setId(nextSequenceService.getNextSequence("restaurants"));
        }
        restaurantRepository.save(restaurants);
    }
    public Page<Restaurant> findByRestaurantName(String restaurantName, Pageable pageable) {
        return restaurantRepository.findByRestaurantName(restaurantName, pageable);
    }
    public void deleteByRestaurantName(String restaurantNames) {
        restaurantRepository.deleteByRestaurantName(restaurantNames);
    }
    public void deleteAll() {
        restaurantRepository.deleteAll();
    }
}

package com.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends PagingAndSortingRepository<Restaurant, Long> {
    Page<Restaurant> findByRestaurantName(String restaurantName, Pageable pageable);
    void deleteByRestaurantName(String restaurantName);
}

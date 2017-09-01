package com.rest;

import com.domain.Restaurant;
import com.service.RestaurantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestaurantInfoRestController {
    private RestaurantInfoService restaurantInfoService;

    @Autowired
    public RestaurantInfoRestController(RestaurantInfoService restaurantInfoService) {
        this.restaurantInfoService = restaurantInfoService;
    }

    @RequestMapping(value = "/restaurant", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void upload(@RequestBody List<Restaurant> restaurants) {
        restaurantInfoService.saveRestaurants(restaurants);
    }

    @RequestMapping(value = "/restaurant/{restaurantName}", method = RequestMethod.GET)
    public Page<Restaurant> findRestaurantByName(@PathVariable String restaurantName,
                                                 @RequestParam(name = "page") Integer page) {
        return restaurantInfoService.findByRestaurantName(restaurantName, new PageRequest(page, 1));
    }

    @RequestMapping(value = "/restaurant/{restaurantName}", method = RequestMethod.DELETE)
    public void deleteByRunningId(@PathVariable String restaurantName) {
        restaurantInfoService.deleteByRestaurantName(restaurantName);
    }

    @RequestMapping(value = "/restaurant", method = RequestMethod.DELETE)
    public void purge() {
        restaurantInfoService.deleteAll();
    }

}

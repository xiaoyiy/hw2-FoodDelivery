package com.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "restaurants")
public class Restaurant {
    @Id
    private long id;
    private String restaurantName;
    private List<MenuItem> menu;
}

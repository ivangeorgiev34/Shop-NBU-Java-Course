package com.ivangeorgiev.shop.domain.entities;

import java.util.Date;
import java.util.UUID;

public class FoodItem extends Item{

    public FoodItem(UUID id, String name, double price, Date expirationDate) {
        super(id, name, price, expirationDate);
    }
}

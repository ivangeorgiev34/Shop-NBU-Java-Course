package com.ivangeorgiev.shop.domain.entities;

import java.util.Date;
import java.util.UUID;

public class FoodItem extends Item{

    public FoodItem(UUID id, String name, double price, Date expirationDate, int quantity) {
        super(id, name, price, expirationDate, quantity);
    }
}

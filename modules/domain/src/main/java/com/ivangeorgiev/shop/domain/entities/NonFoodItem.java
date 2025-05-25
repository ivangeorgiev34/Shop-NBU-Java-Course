package com.ivangeorgiev.shop.domain.entities;

import java.util.Date;
import java.util.UUID;

public class NonFoodItem extends Item{

    public NonFoodItem(UUID id, String name, double price, Date expirationDate, int quantity) {
        super(id, name, price, expirationDate, quantity);
    }
}

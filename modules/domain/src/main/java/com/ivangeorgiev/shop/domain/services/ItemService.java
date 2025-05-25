package com.ivangeorgiev.shop.domain.services;

import com.ivangeorgiev.shop.domain.entities.FoodItem;
import com.ivangeorgiev.shop.domain.entities.Item;
import com.ivangeorgiev.shop.domain.entities.NonFoodItem;

import java.util.Date;
import java.util.UUID;

public class ItemService {
    public Item createItem(UUID id, String name, double price, Date expirationDate, int quantity, boolean isFoodItem){
        return isFoodItem ? new FoodItem(id, name, price, expirationDate, quantity) : new NonFoodItem(id, name, price, expirationDate, quantity);
    }
}

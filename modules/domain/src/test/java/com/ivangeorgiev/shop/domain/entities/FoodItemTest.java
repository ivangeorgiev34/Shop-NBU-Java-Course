package com.ivangeorgiev.shop.domain.entities;

import com.ivangeorgiev.shop.domain.exceptions.NegativeNumberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class FoodItemTest {

    private FoodItem foodItem;
    private Shop shop;
    private final String foodItemId = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    public void setup() throws NegativeNumberException{
        this.foodItem = new FoodItem(UUID.fromString(foodItemId), "burger", 50, new Date(),3);
        this.shop = new Shop(new ArrayList<Cashier>(), new ArrayList<Item>(), new ArrayList<Bill>());
        this.shop.setFoodItemsMarkupPercentage(50);
        this.shop.setNonFoodItemsMarkupPercentage(50);
        FoodItem.setDiscountPercentage(50);
    }

    @Test
    public void finalPrice_discountApplied() throws NegativeNumberException {
        FoodItem.setDaysBeforeActiveDiscount(3);
        Date expDate = Date.from(LocalDate.now().minusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.foodItem.setExpirationDate(expDate);

        double result = this.foodItem.finalPrice(this.shop);
        double expected = 37.5;

        Assertions.assertEquals(expected, result);
    }

    @Test
    public void finalPrice_discountNotApplied() throws NegativeNumberException {
        FoodItem.setDaysBeforeActiveDiscount(3);
        Date expDate = Date.from(LocalDate.now().minusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.foodItem.setExpirationDate(expDate);

        double result = this.foodItem.finalPrice(this.shop);
        double expected = 75;

        Assertions.assertEquals(expected, result);
    }
}

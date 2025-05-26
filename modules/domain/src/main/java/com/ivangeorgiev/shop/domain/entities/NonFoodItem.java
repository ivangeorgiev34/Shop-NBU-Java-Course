package com.ivangeorgiev.shop.domain.entities;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

public class NonFoodItem extends Item{

    public NonFoodItem(UUID id, String name, double price, Date expirationDate, int quantity) {
        super(id, name, price, expirationDate, quantity);
    }

    public double finalPrice(Shop shop){
        long timeDiff = ChronoUnit.DAYS.between(expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now());
        boolean isDiscountApplied = timeDiff <=  (long)daysBeforeActiveDiscount && timeDiff > 0;
        double finalPrice = this.price + (this.price * (shop.getNonFoodItemsMarkupPercentage() / 100));

        return isDiscountApplied
                ? finalPrice - (finalPrice * (discountPercentage / 100))
                : finalPrice;
    }
}

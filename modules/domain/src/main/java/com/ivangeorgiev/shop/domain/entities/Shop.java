package com.ivangeorgiev.shop.domain.entities;

import java.util.List;

public class Shop {

    public Shop(List<Cashier> cashiers, List<Item> items, List<Bill> bills) {
        this.cashiers = cashiers;
        this.items = items;
        this.bills = bills;
    }

    private double foodItemsMarkupPercentage;

    private double nonFoodItemsMarkupPercentage;

    private List<Cashier> cashiers;

    private List<Item> items;

    private List<Bill> bills;

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public double getFoodItemsMarkupPercentage() {
        return foodItemsMarkupPercentage;
    }

    public double getNonFoodItemsMarkupPercentage() {
        return nonFoodItemsMarkupPercentage;
    }

    public void setCashiers(List<Cashier> cashiers) {
        this.cashiers = cashiers;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public void setFoodItemsMarkupPercentage(double foodItemsMarkupPercentage) {
        this.foodItemsMarkupPercentage = foodItemsMarkupPercentage;
    }

    public void setNonFoodItemsMarkupPercentage(double nonFoodItemsMarkupPercentage) {
        this.nonFoodItemsMarkupPercentage = nonFoodItemsMarkupPercentage;
    }

    public List<Item> getSoldItems(){
        return this.items.stream().filter(i -> i.isSold).toList();
    }

    public List<Item> getUnsoldItems(){
        return this.items.stream().filter(i -> !i.isSold).toList();
    }

    public double income(){
        return this.items.stream().mapToDouble(i -> i.finalPrice(this)).sum();
    }
}

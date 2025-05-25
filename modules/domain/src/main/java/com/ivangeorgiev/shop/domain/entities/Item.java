package com.ivangeorgiev.shop.domain.entities;

import com.ivangeorgiev.shop.domain.exceptions.NegativeNumberException;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class Item {

    protected Item(UUID id, String name, double price, Date expirationDate, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
    }

    protected UUID id;

    protected String name;

    protected double price;

    protected Date expirationDate;

    protected static double markupPercentage;

    protected static double discountPercentage;

    protected static int daysBeforeActiveDiscount;

    protected boolean isSold;

    protected int quantity;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public static double getMarkupPercentage() {
        return markupPercentage;
    }

    public static double getDiscountPercentage() {
        return discountPercentage;
    }

    public static double getDaysBeforeActiveDiscount() {
        return daysBeforeActiveDiscount;
    }

    public boolean getIsSold() {
        return isSold;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) throws NegativeNumberException {

        if(price < 0){
            throw new NegativeNumberException("Price cannot be negative");
        }

        this.price = price;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public static void setMarkupPercentage(double value) throws NegativeNumberException{
        if(value < 0){
            throw new NegativeNumberException("Markup percentage cannot be negative");
        }

        markupPercentage = value;
    }

    public static void setDiscountPercentage(double value) throws NegativeNumberException{
        if(value < 0){
            throw new NegativeNumberException("Markup percentage cannot be negative");
        }

        discountPercentage = value;
    }

    public static void setDaysBeforeActiveDiscount(int value) throws NegativeNumberException{
        if(value < 0){
            throw new NegativeNumberException("Markup percentage cannot be negative");
        }

        daysBeforeActiveDiscount = value;
    }

    public void setIsSold(boolean isSold) {
        this.isSold = isSold;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public double finalPrice(){
        long timeDiff = TimeUnit.DAYS.convert(expirationDate.getTime() - new Date().getTime(), TimeUnit.MILLISECONDS);
        boolean isDiscountApplied = timeDiff <=  (long)daysBeforeActiveDiscount && timeDiff > 0;
        double finalPrice = (this.price + (this.price * (markupPercentage * 0.1)));

        return isDiscountApplied
                ? finalPrice - (finalPrice * (discountPercentage * 0.1))
                : finalPrice;
    }

    public boolean isExpired(){
        return this.expirationDate.before(new Date());
    }

    public void increaseQuantity(){
        this.quantity++;
    }

    public void decreaseQuantity(){
        this.quantity--;
    }
}

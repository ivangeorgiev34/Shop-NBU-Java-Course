package com.ivangeorgiev.shop.domain.entities;

import com.ivangeorgiev.shop.domain.exceptions.NegativeNumberException;

import java.util.Date;
import java.util.UUID;

public abstract class Item {

    protected Item(UUID id, String name, double price, Date expirationDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.expirationDate = expirationDate;
    }

    protected UUID id;

    protected String name;

    protected double price;

    protected Date expirationDate;

    protected static double markupPercentage;

    protected boolean isSold;

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

    public boolean getIsSold() {
        return isSold;
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

    public void setIsSold(boolean isSold) {
        this.isSold = isSold;
    }

    protected double finalPrice(){
        return this.price + (this.price * (markupPercentage * 0.1));
    }
}

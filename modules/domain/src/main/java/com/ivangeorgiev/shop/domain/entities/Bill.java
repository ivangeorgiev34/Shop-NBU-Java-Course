package com.ivangeorgiev.shop.domain.entities;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Bill {

    public Bill(UUID id, Cashier publisher, Date publishedDate, List<Item> items) {
        this.id = id;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.items = items;
    }

    private UUID id;

    private Cashier publisher;

    private Date publishedDate;

    private List<Item> items;

    public UUID getId() {
        return id;
    }

    public Cashier getPublisher() {
        return publisher;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public List<Item> getItems(){
        return items;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setPublisher(Cashier publisher) {
        this.publisher = publisher;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setItems(List<Item> items){
        this.items = items;
    }

    public double getItemsTotalPrice(){
        return this.items.stream().mapToDouble(p -> p.getPrice()).sum();
    }
}

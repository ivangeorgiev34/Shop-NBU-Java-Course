package com.ivangeorgiev.shop.domain.entities;

import java.util.List;
import java.util.UUID;

public class Client {
    public Client(UUID id, double balance, String name) {
        this.id = id;
        this.balance = balance;
        this.name = name;
    }

    private UUID id;

    private double balance;

    private String name;

    public UUID getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setName(String name) {
        this.name = name;
    }
}

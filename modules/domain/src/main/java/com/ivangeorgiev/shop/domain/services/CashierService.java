package com.ivangeorgiev.shop.domain.services;

import com.ivangeorgiev.shop.domain.entities.Cashier;

import java.util.UUID;

public class CashierService {
    public Cashier createCashier(String name, double monthlySalary){
        return new Cashier(UUID.randomUUID(), name, monthlySalary);
    }
}

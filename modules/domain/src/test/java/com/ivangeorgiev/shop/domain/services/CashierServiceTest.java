package com.ivangeorgiev.shop.domain.services;

import com.ivangeorgiev.shop.domain.entities.Cashier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class CashierServiceTest {

    private CashierService cashierService;

    @BeforeEach
    public void setup(){
        this.cashierService = new CashierService();
    }

    @Test
    public void createCashier_shouldCreate(){
        UUID id = UUID.fromString("da0df22c-1934-4d93-acc8-066f9d9647db");
        String name = "example";
        double monthlySalary = 20.50;

        Cashier cashier = this.cashierService.createCashier(id, name, monthlySalary);

        Assertions.assertEquals(id, cashier.getId());
        Assertions.assertEquals(name, cashier.getName());
        Assertions.assertEquals(monthlySalary, cashier.getMonthlySalary());
    }
}

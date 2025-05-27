package com.ivangeorgiev.shop.domain.services;

import com.ivangeorgiev.shop.domain.entities.*;
import com.ivangeorgiev.shop.domain.exceptions.NegativeNumberException;
import com.ivangeorgiev.shop.domain.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ShopService {
    private final Shop shop;
    private final CashierService cashierService;
    private final ItemService itemService;

    public ShopService(Shop shop, CashierService cashierService, ItemService itemService){
        this.shop = shop;
        this.cashierService = cashierService;
        this.itemService = itemService;
    }

    public Shop getShop(){
        return shop;
    }

    private double cashierSalariesSum(){
        return this.shop.getCashiers()
                .stream()
                .mapToDouble(Cashier::getMonthlySalary)
                .sum();
    }

    private double itemDeliverySum(){
        return this.shop.getItems()
                .stream()
                .mapToDouble(i -> i.getPrice() * i.getOriginalQuantity())
                .sum();
    }

    public double getIncome(){
        double soldItemsSum = this.shop.getBills()
                .stream()
                .mapToDouble(b -> {
                    List<Map.Entry<String, Integer>> entryList =
                            new ArrayList<>(b.getItemQuantities().entrySet());

                    double sum = 0;

                    for(Map.Entry<String, Integer> entry : entryList){

                        String itemName = entry.getKey();
                        double itemQuantity = entry.getValue();

                        Optional<Item> currItem = this.shop.getItems()
                                .stream()
                                .filter(i -> i.getName().equals(itemName))
                                .findFirst();

                        if(currItem.isEmpty()){
                            continue;
                        }

                        sum += currItem.get().finalPrice(this.shop) * itemQuantity;
                    }

                    return sum;

                })
                .sum();

        return soldItemsSum;
    }

    public double getExpenses(){
        double cashierSalariesSum = cashierSalariesSum();
        double itemDeliverySum = itemDeliverySum();

        return cashierSalariesSum + itemDeliverySum;
    }

    public double getProfit(){
        return getIncome() - getExpenses();
    }

    public void createShop(Scanner scanner) throws NegativeNumberException, ParseException, Exception {
        System.out.println("Please enter a discount percentage that will be applied for all items in the shop: ");
        double discountPercentage = scanner.nextDouble();
        Utils.checkForNonPositiveNumber(discountPercentage);
        Item.setDiscountPercentage(discountPercentage);

        System.out.println("Please enter number of days before expiration date of items when discount will be applied: ");
        int daysBeforeActiveDiscount = scanner.nextInt();
        Utils.checkForNonPositiveNumber(daysBeforeActiveDiscount);
        Item.setDaysBeforeActiveDiscount(daysBeforeActiveDiscount);

        System.out.println("Please enter the number of cashiers that the shop will have: ");
        int cashierCount = scanner.nextInt();

        if(cashierCount <= 0){
            throw new Exception("There must be at least one cashier!");
        }

        for (int i = 0; i < cashierCount; i++) {
            System.out.println("Please enter the information for cashier number " + (i + 1) + " : ");

            System.out.println("Name: ");
            String name = scanner.next();

            System.out.println("Monthly salary: ");
            double monthlySalary = scanner.nextDouble();

            this.shop.getCashiers().add(cashierService.createCashier(UUID.randomUUID(), name, monthlySalary));
        }

        System.out.println("Please enter the number of items that the shop will have: ");
        int itemsCount = scanner.nextInt();

        if(itemsCount <= 0){
            throw new Exception("There must be at least one item!");
        }

        for (int i = 0; i < itemsCount; i++) {
            System.out.println("Please enter the information for item number " + (i + 1) + " : ");

            this.itemService.addNewItemToShop(scanner, this.shop);
        }

        System.out.println("Shop created successfully!");
    }

    public int listItems(){
        List<Item> items = this.shop.getItems().stream().filter(i -> !i.getIsSold() && !i.isExpired() && i.getQuantity() > 0).toList();

        for(Item item : items){
            System.out.println("Name: " + item.getName());
        }

        return items.size();
    }

    public void addBill(Bill bill){
        this.shop.getBills().add(bill);
    }
}

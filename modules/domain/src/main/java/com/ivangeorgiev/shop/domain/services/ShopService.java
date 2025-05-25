package com.ivangeorgiev.shop.domain.services;

import com.ivangeorgiev.shop.domain.entities.*;
import com.ivangeorgiev.shop.domain.exceptions.NegativeNumberException;

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

    public void createShop(Scanner scanner) throws NegativeNumberException, ParseException, Exception {
        System.out.println("Hello! Please create a shop first!");

        System.out.println("Please enter a markup percentage that will be applied for all food items in the shop: ");
        double foodItemMarkup = scanner.nextDouble();
        checkForNonPositiveNumber(foodItemMarkup);
        FoodItem.setMarkupPercentage(foodItemMarkup);

        System.out.println("Please enter a markup percentage that will be applied for all non food items in the shop: ");
        double nonFoodItemMarkup = scanner.nextDouble();
        checkForNonPositiveNumber(nonFoodItemMarkup);
        NonFoodItem.setMarkupPercentage(nonFoodItemMarkup);

        System.out.println("Please enter a discount percentage that will be applied for all items in the shop: ");
        double discountPercentage = scanner.nextDouble();
        checkForNonPositiveNumber(discountPercentage);
        Item.setDiscountPercentage(discountPercentage);

        System.out.println("Please enter number of days before expiration date of items when discount will be applied: ");
        int daysBeforeActiveDiscount = scanner.nextInt();
        checkForNonPositiveNumber(daysBeforeActiveDiscount);
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

            System.out.println("Name: ");
            String name = scanner.next();

            System.out.println("Price: ");
            double price = scanner.nextDouble();

            System.out.println("Expiration date (in the following format: dd/MM/yyyy (16/04/2025)): ");
            String expirationDateString = scanner.next();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date expirationDate = formatter.parse(expirationDateString);

            System.out.println("Is it a food or non food item (type food or nonfood): ");
            String itemType = scanner.next();

            boolean isFoodItem;

            if (itemType.equals("food")){
                isFoodItem = true;
            }else if (itemType.equals("nonfood")){
                isFoodItem = false;
            }else{
                throw new Exception("Invalid input, choose food or nonfood!");
            }

            System.out.println("Quantity: ");
            int quantity = scanner.nextInt();

            this.shop.getItems().add(this.itemService.createItem(UUID.randomUUID(), name, price, expirationDate, quantity, isFoodItem));
        }

        System.out.println("Shop created successfully!");
    }

    public void listItems(){
        for(Item item : this.shop.getItems().stream().filter(i -> !i.getIsSold() && !i.isExpired() && i.getQuantity() > 0).toList()){
            System.out.println("Name: " + item.getName());
        }
    }

    public void addBill(Bill bill){
        this.shop.getBills().add(bill);
    }

    private void checkForNonPositiveNumber(double num) throws NegativeNumberException{
        if(num <= 0){
            throw new NegativeNumberException("Cannot enter non-positive number");
        }
    }

}

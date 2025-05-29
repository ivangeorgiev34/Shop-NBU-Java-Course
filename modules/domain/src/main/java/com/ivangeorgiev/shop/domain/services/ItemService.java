package com.ivangeorgiev.shop.domain.services;

import com.ivangeorgiev.shop.domain.entities.FoodItem;
import com.ivangeorgiev.shop.domain.entities.Item;
import com.ivangeorgiev.shop.domain.entities.NonFoodItem;
import com.ivangeorgiev.shop.domain.entities.Shop;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

public class ItemService {
    public Item createItem(UUID id, String name, double price, Date expirationDate, int quantity, boolean isFoodItem){
        return isFoodItem ? new FoodItem(id, name, price, expirationDate, quantity) : new NonFoodItem(id, name, price, expirationDate, quantity);
    }

        public void addNewItemToShop(Scanner scanner, Shop shop) throws Exception{
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

        shop.getItems().add(createItem(UUID.randomUUID(), name, price, expirationDate, quantity, isFoodItem));
    }
}

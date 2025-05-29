package com.ivangeorgiev.shop.domain.services;

import com.ivangeorgiev.shop.domain.entities.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.*;

public class ItemServiceTest {

    private ItemService itemService;
    private Scanner scannerMock;
    private Shop shop;

    private final UUID itemId = UUID.randomUUID();
    private final String itemName = "item";
    private final double itemPrice = 50.50;
    private final Date itemExpirationDate = new Date();
    private final int itemQuantity = 5;

    @BeforeEach
    public void setup(){
        this.itemService = new ItemService();
        this.scannerMock = mock(Scanner.class);
        this.shop = new Shop(new ArrayList<Cashier>(), new ArrayList<Item>(), new ArrayList<Bill>());
    }

    @Test
    public void createItem_foodItem(){
        boolean isFoodItem = true;

        Item foodItem = this.itemService.createItem(this.itemId, this.itemName, this.itemPrice, this.itemExpirationDate, this.itemQuantity, isFoodItem);

        Assertions.assertInstanceOf(FoodItem.class, foodItem);
    }

    @Test
    public void createItem_nonFoodItem(){
        boolean isFoodItem = false;

        Item foodItem = this.itemService.createItem(this.itemId, this.itemName, this.itemPrice, this.itemExpirationDate, this.itemQuantity, isFoodItem);

        Assertions.assertInstanceOf(NonFoodItem.class, foodItem);
    }

    @Test
    public void addNewItemToShop_foodItem() throws Exception{
        when(this.scannerMock.next()).thenReturn(this.itemName, "11/11/2025", "food");
        when(this.scannerMock.nextDouble()).thenReturn(this.itemPrice);
        when(this.scannerMock.nextInt()).thenReturn(this.itemQuantity);

        this.itemService.addNewItemToShop(this.scannerMock, this.shop);

        Optional<Item> item = this.shop.getItems()
                .stream()
                .findFirst();

        if(item.isEmpty()){
            Assertions.fail("Item was not added to the shop's items list");
        }

        Assertions.assertEquals(this.itemName, item.get().getName());
        Assertions.assertInstanceOf(FoodItem.class, item.get());
    }

    @Test
    public void addNewItemToShop_nonfoodItem() throws Exception{
        when(this.scannerMock.next()).thenReturn(this.itemName, "11/11/2025", "nonfood");
        when(this.scannerMock.nextDouble()).thenReturn(this.itemPrice);
        when(this.scannerMock.nextInt()).thenReturn(this.itemQuantity);

        this.itemService.addNewItemToShop(this.scannerMock, this.shop);

        Optional<Item> item = this.shop.getItems()
                .stream()
                .findFirst();

        if(item.isEmpty()){
            Assertions.fail("Item was not added to the shop's items list");
        }

        Assertions.assertEquals(this.itemName, item.get().getName());
        Assertions.assertInstanceOf(NonFoodItem.class, item.get());
    }

    @Test()
    public void addNewItemToShop_throwsException() throws Exception{
        when(this.scannerMock.next()).thenReturn(this.itemName, "11/11/2025", "");
        when(this.scannerMock.nextDouble()).thenReturn(this.itemPrice);
        when(this.scannerMock.nextInt()).thenReturn(this.itemQuantity);

        Assertions.assertThrows(Exception.class, () -> this.itemService.addNewItemToShop(this.scannerMock, this.shop));
    }
}

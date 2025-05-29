package com.ivangeorgiev.shop.domain.services;

import com.ivangeorgiev.shop.domain.entities.*;
import com.ivangeorgiev.shop.domain.exceptions.NegativeNumberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

import static org.mockito.Mockito.*;

public class ShopServiceTest {

    private ItemService itemService;
    private CashierService cashierService;
    private ShopService shopService;
    private Shop shop;
    private Scanner scanner;

    @BeforeEach
    public void setup(){
        this.itemService = new ItemService();
        this.cashierService = new CashierService();
        this.shop = mock(Shop.class);
        this.scanner = mock(Scanner.class);
        this.shopService = new ShopService(this.shop, this.cashierService, this.itemService);
    }

    @Test
    public void getExpenses_returnExpenses(){
        Cashier cashierOne = new Cashier(UUID.randomUUID(), "", 30.2);
        Cashier cashierTwo = new Cashier(UUID.randomUUID(), "", 20.3);

        List<Cashier> cashiers = new ArrayList<Cashier>();
        cashiers.add(cashierOne);
        cashiers.add(cashierTwo);

        when(this.shop.getCashiers()).thenReturn(cashiers);

        FoodItem itemOne = new FoodItem(UUID.randomUUID(), "", 30.5, new Date(), 1);
        NonFoodItem itemTwo = new NonFoodItem(UUID.randomUUID(), "", 20, new Date(), 1);

        List<Item> items = new ArrayList<Item>();
        items.add(itemOne);
        items.add(itemTwo);

        when(this.shop.getItems()).thenReturn(items);

        double expectedExpenses = 101;

        Assertions.assertEquals(expectedExpenses, this.shopService.getExpenses());
    }

    @Test
    public void getIncome_returnIncome(){
        FoodItem itemOne = new FoodItem(UUID.randomUUID(), "one", 50, new Date(), 2);
        NonFoodItem itemTwo = new NonFoodItem(UUID.randomUUID(), "two", 50, new Date(), 2);

        HashMap<String, Integer> itemQuantityOne = new HashMap<String, Integer>();
        itemQuantityOne.put("one", 2);

        HashMap<String, Integer> itemQuantityTwo = new HashMap<String, Integer>();
        itemQuantityTwo.put("two", 2);

        Date date = Date.from(LocalDate.now().plusDays(5).atStartOfDay().toInstant(ZoneOffset.UTC));

        List<Bill> bills = new ArrayList<Bill>();
        Bill billOne = new Bill(UUID.randomUUID(),new Cashier(UUID.randomUUID(), "", 1), date, new ArrayList<Item>(), itemQuantityOne);
        Bill billTwo = new Bill(UUID.randomUUID(),new Cashier(UUID.randomUUID(), "", 1), date, new ArrayList<Item>(), itemQuantityTwo);

        bills.add(billOne);
        bills.add(billTwo);

        List<Item> listOne = new ArrayList<Item>();
        listOne.add(itemOne);

        List<Item> listTwo = new ArrayList<Item>();
        listTwo.add(itemTwo);

        when(this.shop.getItems()).thenReturn(listOne, listTwo);
        when(this.shop.getBills()).thenReturn(bills);

        double expectedIncome = 200;

        Assertions.assertEquals(expectedIncome, this.shopService.getIncome());
    }

    @Test
    public void createShop_noCashierShouldThrowException(){
        when(this.scanner.nextDouble()).thenReturn(1d, 2d);
        when(this.scanner.nextInt()).thenReturn(1, 0);

        Assertions.assertThrows(Exception.class, () -> this.shopService.createShop(this.scanner), "There must be at least one cashier!");
    }

    @Test
    public void createShop_noItemsShouldCreate(){
        when(this.scanner.nextDouble()).thenReturn(1d, 2d, 50.50);
        when(this.scanner.nextInt()).thenReturn(1, 1, 0);
        when(this.scanner.next()).thenReturn("");

        Assertions.assertThrows(Exception.class, () -> this.shopService.createShop(this.scanner), "There must be at least one item!");
    }

    @Test
    public void createShop_shouldCreate() throws ParseException, Exception, NegativeNumberException {
        String cashierName = "cashier";
        String itemName = "item";
        when(this.scanner.nextDouble()).thenReturn(1d, 2d, 50.50, 2d);
        when(this.scanner.nextInt()).thenReturn(1, 1, 1, 1);
        when(this.scanner.next()).thenReturn(cashierName, itemName, "11/11/2025", "food");

        when(this.shop.getCashiers()).thenReturn(new ArrayList<Cashier>());
        when(this.shop.getItems()).thenReturn(new ArrayList<Item>());

        this.shopService.createShop(this.scanner);

        Optional<Cashier> cashier = this.shop.getCashiers()
                .stream()
                .findFirst();

        if(cashier.isEmpty()){
            Assertions.fail("Cashier was not found in the list of cashiers in the shop");
        }

        Optional<Item> item = this.shop.getItems()
                .stream()
                .findFirst();

        if(item.isEmpty()){
            Assertions.fail("Item was not found in the list of items in the shop");
        }

        Assertions.assertEquals(cashierName, cashier.get().getName());
        Assertions.assertEquals(itemName, item.get().getName());
    }

    @Test
    public void listItems(){
        List<Item> items = new ArrayList<Item>();

        FoodItem itemOne = new FoodItem(UUID.randomUUID(), "", 2.1, new Date(), 1);
        itemOne.setIsSold(false);

        FoodItem itemTwo = new FoodItem(UUID.randomUUID(), "", 2.1, new Date(), 1);
        itemTwo.setExpirationDate(Date.from(LocalDate.now().minusDays(2).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        FoodItem itemThree = new FoodItem(UUID.randomUUID(), "", 2.1, new Date(), 0);

        items.add(itemOne);
        items.add(itemTwo);
        items.add(itemThree);

        when(this.shop.getItems()).thenReturn(items);

        int expectedCount = 0;

        Assertions.assertEquals(expectedCount, this.shopService.listItems());
    }
}

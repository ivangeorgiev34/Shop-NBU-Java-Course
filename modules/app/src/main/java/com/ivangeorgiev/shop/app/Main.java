package com.ivangeorgiev.shop.app;

import com.ivangeorgiev.shop.domain.entities.*;
import com.ivangeorgiev.shop.domain.exceptions.NegativeNumberException;
import com.ivangeorgiev.shop.domain.services.CashierService;
import com.ivangeorgiev.shop.domain.services.ItemService;
import com.ivangeorgiev.shop.domain.services.ShopService;
import com.ivangeorgiev.shop.domain.utils.Utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Hello! Please create a shop first!");

            System.out.println("Please enter a markup percentage that will be applied for all food items in the shop: ");
            double foodItemMarkup = scanner.nextDouble();
            Utils.checkForNonPositiveNumber(foodItemMarkup);

            System.out.println("Please enter a markup percentage that will be applied for all non food items in the shop: ");
            double nonFoodItemMarkup = scanner.nextDouble();
            Utils.checkForNonPositiveNumber(nonFoodItemMarkup);

            ItemService itemService = new ItemService();
            ShopService shopService = new ShopService(new Shop(new ArrayList<Cashier>(),new ArrayList<Item>(), new ArrayList<Bill>(), foodItemMarkup, nonFoodItemMarkup),new CashierService(), itemService);

            shopService.createShop(scanner);
            Client client = registerClient(scanner);

            showMainMenu(scanner, client, shopService, itemService);

        } catch(InputMismatchException exc){
            System.out.println("Incorrect form of input data");
        } catch (Exception exc){
            System.out.println(exc.getMessage());
        }
    }

    private static Client registerClient(Scanner scanner){
        System.out.println("Now a client is need to be created, so please enter your information: ");

        System.out.println("Client name: ");
        String name = scanner.next();

        System.out.println("Client balance: ");
        double balance = scanner.nextDouble();

        return new Client(UUID.randomUUID(),balance, name);
    }

    private static void showMainMenu(Scanner scanner, Client client, ShopService shopService, ItemService itemService) throws Exception{
        while(true){
            System.out.println("\n=== Store Management System ===");
            System.out.println("1. Add money to client's account");
            System.out.println("2. Shop");
            System.out.println("3. View a bill");
            System.out.println("4. Add new item to shop");
            System.out.println("5. View expenses");
            System.out.println("6. View income");
            System.out.println("7. View profit");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice){
                case 1:
                    System.out.print("How much money do you need to add: ");
                    double balance = scanner.nextDouble();
                    client.setBalance(client.getBalance() + balance);
                    break;
                case 2:
                    shop(scanner, shopService, client);
                    break;
                case 3:
                    viewBill(scanner, shopService, client);
                    break;
                case 4:
                    itemService.addNewItemToShop(scanner,shopService.getShop());
                    break;
                case 5:
                    viewExpenses(shopService);
                    break;
                case 6:
                    viewIncome(shopService);
                    break;
                case 7:
                    viewProfit(shopService);
                    break;
                case 0:
                    System.out.println("Exiting the system...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println("Available balance: " + client.getBalance());
        }
    }

    private static void viewIncome(ShopService shopService){
        double shopExpenses = shopService.getIncome();

        System.out.println("Total income: " + shopExpenses);
    }

    private static void viewExpenses(ShopService shopService){
        double shopExpenses = shopService.getExpenses();

        System.out.println("Total expenses: " + shopExpenses);
    }

    private static void viewProfit(ShopService shopService){
        double shopProfit = shopService.getProfit();

        System.out.println("Total profit: " + shopProfit);
    }

    private static void shop(Scanner scanner, ShopService shopService, Client client) throws Exception{
        Random random = new Random();
        int randomIndex = random.nextInt(shopService.getShop().getCashiers().size());
        Cashier cashier = shopService.getShop().getCashiers().get(randomIndex);

        System.out.println("You are being serviced by: " + cashier.getName());
        System.out.println("List of products:");
        int itemsCount = shopService.listItems();

        if(itemsCount == 0){
            System.out.println("No products available");
            return;
        }

        List<Item> items = new ArrayList<Item>();
        HashMap<String, Integer> itemQuantitiesMap = new HashMap<String, Integer>();

        while(true){
            System.out.println("Type the name of the item you want or type 0 if you want to stop choosing items: ");

            String name = scanner.next();

            if(name.equals("0")){
                break;
            }

            if(shopService.getShop().getItems().stream().noneMatch(i -> i.getName().equals(name))){
                System.out.println("Item with such name does not exist");
                continue;
            }

            Optional<Item> item = shopService.getShop().getItems().stream().filter(i -> i.getName().equals(name) && !i.getIsSold() && !i.isExpired()).findFirst();

            if(item.isEmpty()){
                System.out.println("Item is either expired or sold out");
                continue;
            }

            System.out.println("Quantity: ");
            int quantity = scanner.nextInt();

            if(item.get().getQuantity() - quantity < 0){
                throw new Exception("Not enough quantity");
            }

            if(client.getBalance() < item.get().finalPrice(shopService.getShop())){
                System.out.println("Money is not enough");
                continue;
            }

            client.setBalance(client.getBalance() - item.get().finalPrice(shopService.getShop()));

            item.get().setQuantity(item.get().getQuantity() - quantity);

            if(item.get().getQuantity() == 0){
                item.get().setIsSold(true);
            }

            if(!itemQuantitiesMap.containsKey(item.get().getName())){
                itemQuantitiesMap.put(item.get().getName(), 0);
            }

            itemQuantitiesMap.put(item.get().getName(), itemQuantitiesMap.get(item.get().getName()) + quantity);

            items.add(item.get());
        }

        Bill bill = new Bill(UUID.randomUUID(), cashier, new Date(), items, itemQuantitiesMap);

        shopService.addBill(bill);

        bill.saveToFile(shopService.getShop());
    }

    private static void viewBill(Scanner scanner, ShopService shopService, Client client) throws IOException, ClassNotFoundException {
        System.out.println("Please choose the number of bill you want to view");

        int num = 1;
        HashMap<Integer, Bill> map = new HashMap<Integer, Bill>();
        for(Bill bill : shopService.getShop().getBills()){
            System.out.println(num + ": " + bill.getId() + ", " + bill.getPublishedDate());
            map.put(num, bill);
            num++;
        }

        int choice = scanner.nextInt();
        Bill deserialized = map.get(choice).deserialize();

        System.out.println(deserialized.generateReceiptText(shopService.getShop()));
    }
}
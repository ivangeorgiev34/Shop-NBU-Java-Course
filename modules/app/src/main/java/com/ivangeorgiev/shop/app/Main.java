package com.ivangeorgiev.shop.app;

import com.ivangeorgiev.shop.domain.entities.*;
import com.ivangeorgiev.shop.domain.exceptions.NegativeNumberException;
import com.ivangeorgiev.shop.domain.services.CashierService;
import com.ivangeorgiev.shop.domain.services.ItemService;
import com.ivangeorgiev.shop.domain.services.ShopService;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        try {
            ShopService shopService = new ShopService(new Shop(new ArrayList<Cashier>(),new ArrayList<Item>(), new ArrayList<Bill>()),new CashierService(), new ItemService());

            Scanner scanner = new Scanner(System.in);

            shopService.createShop(scanner);
            Client client = registerClient(scanner);

            showMainMenu(scanner, client, shopService);


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

    private static void showMainMenu(Scanner scanner, Client client, ShopService shopService){
        while(true){
            System.out.println("\n=== Store Management System ===");
            System.out.println("1. Add money");
            System.out.println("2. Shop");
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
                case 0:
                    System.out.println("Exiting the system...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void shop(Scanner scanner, ShopService shopService, Client client){
        Random random = new Random();
        int randomIndex = random.nextInt(shopService.getShop().getCashiers().size());
        Cashier cashier = shopService.getShop().getCashiers().get(randomIndex);

        System.out.println("You are being serviced by: " + cashier.getName());
        System.out.println("List of products:");
        shopService.listItems();

        String input = "";
        List<Item> items = new ArrayList<Item>();
        while(!input.equals("0")){
            System.out.println("Type the name of the item you want or type 0 if you want to stop choosing items: ");

            input = scanner.next();
            final String name = input;

            if(shopService.getShop().getItems().stream().noneMatch(i -> i.getName().equals(name))){
                System.out.println("Wrong name");
                continue;
            }

            Optional<Item> item = shopService.getShop().getItems().stream().filter(i -> i.getName() == name && !i.getIsSold()).findFirst();

            if(client.getBalance() < item.get().finalPrice()){
                System.out.println("Money is not enough");
                continue;
            }

            if(item.get().getQuantity() == 0){
                System.out.println("Item is no longer for sale");
                item.get().setIsSold(true);
                continue;
            }

            item.get().setQuantity(item.get().getQuantity() - 1);

            items.add(item.get());
        }

        Bill bill = new Bill(UUID.randomUUID(),cashier,new Date(),items);
    }
}
package com.ivangeorgiev.shop.domain.services;

import com.ivangeorgiev.shop.domain.entities.Client;

import java.util.Scanner;
import java.util.UUID;

public class ClientService {
    public Client createClient(Scanner scanner){
        System.out.println("Now a client is need to be created, so please enter your information: ");

        System.out.println("Client name: ");
        String name = scanner.next();

        System.out.println("Client balance: ");
        double balance = scanner.nextDouble();

        return new Client(UUID.randomUUID(),balance, name);
    }
}

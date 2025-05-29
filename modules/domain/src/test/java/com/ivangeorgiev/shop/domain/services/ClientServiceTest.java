package com.ivangeorgiev.shop.domain.services;

import com.ivangeorgiev.shop.domain.entities.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    private ClientService clientService;
    private Scanner scannerMock;

    private final String clientName = "client";
    private final double clientBalance = 50.50;

    @BeforeEach
    public void setup(){
        this.clientService = new ClientService();
        this.scannerMock = mock(Scanner.class);

        when(this.scannerMock.next()).thenReturn(this.clientName);
        when(this.scannerMock.nextDouble()).thenReturn(this.clientBalance);
    }

    @Test
    public void createClient_shouldCreate(){
        Client client = this.clientService.createClient(this.scannerMock);

        Assertions.assertEquals(this.clientName, client.getName());
        Assertions.assertEquals(this.clientBalance, client.getBalance());
    }
}

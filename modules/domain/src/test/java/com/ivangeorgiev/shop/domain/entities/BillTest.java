package com.ivangeorgiev.shop.domain.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class BillTest {

    private Bill bill;
    private final String billId = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    public void setup(){

        List<Item> items = new ArrayList<>();
        items.add(new FoodItem(UUID.randomUUID(),"",2, new Date(),1));

        bill = new Bill(UUID.fromString(this.billId),new Cashier(UUID.randomUUID(),"",2),new Date(),items, new HashMap<String, Integer>());
    }

    @Test
    public void serialize_shouldSerialize() throws IOException {
        String fileName = "receipt_" + bill.getId() + ".bin";
        Path path = Path.of("src", "test", "java", "com", "ivangeorgiev", "shop", "domain", "entities");

        bill.serialize(path);

        String filePath = path.toString() + "/" + fileName;

        File file = new File(filePath);
        Assertions.assertTrue(file.exists());

        file.delete();
    }

    @Test
    public void deserialize_shouldDeserialize() throws IOException, ClassNotFoundException {
        String fileName = "receipt_" + bill.getId() + ".bin";
        Path path = Path.of("src", "test", "java", "com", "ivangeorgiev", "shop", "domain", "entities");

        String filePath = path.toString() + "/" + fileName;
        File file = new File(filePath);

        bill.serialize(path);
        Bill deserializedBill = bill.deserialize(path);

        Assertions.assertEquals(bill.getId().toString(), deserializedBill.getId().toString());

        file.delete();
    }
}

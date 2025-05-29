package com.ivangeorgiev.shop.domain.entities;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Bill implements Serializable{

    public Bill(UUID id, Cashier publisher, Date publishedDate, List<Item> items, HashMap<String, Integer> itemQuantities) {
        this.id = id;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.items = items;
        this.itemQuantities = itemQuantities;
    }

    private UUID id;

    private Cashier publisher;

    private Date publishedDate;

    private List<Item> items;

    private HashMap<String, Integer> itemQuantities;

    public UUID getId() {
        return id;
    }

    public Cashier getPublisher() {
        return publisher;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public List<Item> getItems(){
        return items;
    }

    public HashMap<String, Integer> getItemQuantities() {
        return itemQuantities;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setPublisher(Cashier publisher) {
        this.publisher = publisher;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setItems(List<Item> items){
        this.items = items;
    }

    public double getItemsTotalPrice(Shop shop){
        return this.items.stream().mapToDouble(p -> p.finalPrice(shop) * itemQuantities.get(p.getName())).sum();
    }

    public void saveToFile(Shop shop) throws IOException {
        String filename = "receipt_" + id + ".txt";
        Path filePath = Paths.get("modules","domain", "src", "main", "resources", "bills", filename).toAbsolutePath();

        Files.createDirectories(filePath.getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(generateReceiptText(shop));
        }

        serialize(filePath.getParent());
    }

    public Bill deserialize(Path path) throws IOException, ClassNotFoundException{
        Path filePath = path.resolve("receipt_" + id + ".bin");

        try (ObjectInputStream ois = new ObjectInputStream(
                Files.newInputStream(filePath))) {
            return (Bill) ois.readObject();
        }
    }

    public void serialize(Path path) throws IOException {
        Path binPath = path.resolve("receipt_" + id + ".bin");
        try (ObjectOutputStream oos = new ObjectOutputStream(
                Files.newOutputStream(binPath, StandardOpenOption.CREATE_NEW))) {
            oos.writeObject(this);
        }
    }

    public String generateReceiptText(Shop shop) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();

        sb.append("=== RECEIPT ===\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Date: ").append(dateFormat.format(publishedDate)).append("\n");
        sb.append("Cashier id: ").append(publisher.getId().toString()).append("\n");
        sb.append("Cashier name: ").append(publisher.getName()).append("\n");
        sb.append("----------------------------\n");

        for (Item item : items) {
            int quantity = itemQuantities.get(item.getName());
            sb.append(String.format("%-1s x%-6d %6.2f\n",
                    item.getName(), quantity, item.finalPrice(shop) * quantity));
        }

        sb.append("----------------------------\n");
        sb.append(String.format("TOTAL: %22.2f\n", getItemsTotalPrice(shop)));
        sb.append("============================");

        return sb.toString();
    }
}

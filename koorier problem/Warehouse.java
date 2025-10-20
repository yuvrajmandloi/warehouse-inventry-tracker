import java.io.*;
import java.util.*;

public class Warehouse {
    private Map<String, Product> inventory;
    private final StockObserver observer ;
    private final String FILE_NAME = "inventory.txt";

    public Warehouse(StockObserver observer) {
        this.observer = observer;
        this.inventory = new HashMap<>();
        loadInventoryFromFile(); 
    }

    public synchronized void addProduct(String id, String name, int quantity, int threshold) {
        if (inventory.containsKey(id)) {
            System.out.println(" Product with ID " + id + " already exists!");
            return;
        }
        Product product = new Product(id, name, quantity, threshold);
        inventory.put(id, product);
        System.out.println(" Added product: " + name);
        saveInventoryToFile();
    }

    public synchronized void receiveShipment(String id, int amount) {
        Product product = inventory.get(id);
        if (product == null) {
            System.out.println(" Invalid product ID!");
            return;
        }
        product.setQuantity(product.getQuantity() + amount);
        System.out.println(" Received " + amount + " units of " + product.getName() +
                ". Total now: " + product.getQuantity());
        saveInventoryToFile();
    }

    public synchronized void fulfillOrder(String id, int amount) {
        Product product = inventory.get(id);
        if (product == null) {
            System.out.println(" Invalid product ID!");
            return;
        }
        if (product.getQuantity() < amount) {
            System.out.println(" Insufficient stock for " + product.getName());
            return;
        }
        product.setQuantity(product.getQuantity() - amount);
        System.out.println(" Fulfilled order of " + amount + " units for " + product.getName() +
                ". Remaining: " + product.getQuantity());

        if (product.getQuantity() < product.getThreshold()) {
            observer.onLowStock(product);
        }
        saveInventoryToFile();
    }

    public synchronized void displayInventory() {
        System.out.println("\n Current Warehouse Inventory:");
        for (Product p : inventory.values()) {
            System.out.println(p);
        }
        System.out.println();
    }

    private synchronized void saveInventoryToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(inventory);
        } catch (IOException e) {
            System.out.println(" Error saving inventory: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized void loadInventoryFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            inventory = (HashMap<String, Product>) in.readObject();
            System.out.println(" Inventory loaded from file.");
        } catch (Exception e) {
            System.out.println(" Error loading inventory: " + e.getMessage());
        }
    }
}

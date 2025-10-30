
    import java.util.*;

class Product {
    private String id;
    private String name;
    private int quantity;
    private int threshold;

    public Product(String id, String name, int quantity, int threshold) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.threshold = threshold;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public int getThreshold() { return threshold; }

    public void increaseStock(int amount) { this.quantity += amount; }
    public void decreaseStock(int amount) { this.quantity -= amount; }

    public boolean isLowStock() {
        return quantity < threshold;
    }

    @Override
    public String toString() {
        return String.format("Product[ID=%s, Name=%s, Qty=%d, Threshold=%d]", id, name, quantity, threshold);
    }
}

interface StockObserver {
    void onLowStock(Product product);
}

class AlertService implements StockObserver {
    @Override
    public void onLowStock(Product product) {
        System.out.println(" ALERT: Low stock for " + product.getName() + 
                           "  only " + product.getQuantity() + " left!");
    }
}

class Warehouse {
    private Map<String, Product> inventory = new HashMap<>();
    private List<StockObserver> observers = new ArrayList<>();

    public void addObserver(StockObserver observer) {
        observers.add(observer);
    }

    public void addProduct(Product product) {
        if (inventory.containsKey(product.getId())) {
            System.out.println(" Product already exists with ID: " + product.getId());
            return;
        }
        inventory.put(product.getId(), product);
        System.out.println(" Product added: " + product);
    }

    public void receiveShipment(String productId, int quantity) {
        Product product = inventory.get(productId);
        if (product == null) {
            System.out.println(" Invalid product ID: " + productId);
            return;
        }
        product.increaseStock(quantity);
        System.out.println(" Received " + quantity + " units of " + product.getName());
        checkStock(product);
    }

    public void fulfillOrder(String productId, int quantity) {
        Product product = inventory.get(productId);
        if (product == null) {
            System.out.println(" Invalid product ID: " + productId);
            return;
        }
        if (product.getQuantity() < quantity) {
            System.out.println(" Insufficient stock for " + product.getName());
            return;
        }
        product.decreaseStock(quantity);
        System.out.println(" Fulfilled order of " + quantity + " units for " + product.getName());
        checkStock(product);
    }

    private void checkStock(Product product) {
        if (product.isLowStock()) {
            for (StockObserver observer : observers) {
                observer.onLowStock(product);
            }
        }
    }

    public void showInventory() {
        System.out.println("\n Current Inventory:");
        for (Product p : inventory.values()) {
            System.out.println("   " + p);
        }
    }
}

public class main {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();
        AlertService alertService = new AlertService();
        warehouse.addObserver(alertService);

        Product laptop = new Product("P001", "Laptop", 0, 5);
        warehouse.addProduct(laptop);

        warehouse.receiveShipment("P001", 10);  // +10 units
        warehouse.fulfillOrder("P001", 6);      // -6 units
        warehouse.showInventory();

        Product phone = new Product("P002", "Smartphone", 3, 4);
        warehouse.addProduct(phone);
        warehouse.fulfillOrder("P002", 1);

        warehouse.showInventory();
    }
}

    


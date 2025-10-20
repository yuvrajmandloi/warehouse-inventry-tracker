public class Main {
    public static void main(String[] args) {
        StockObserver alertService = new AlertService();
        Warehouse warehouse = new Warehouse(alertService);

        warehouse.addProduct("P001", "Laptop", 10, 5);
        warehouse.addProduct("P002", "Phone", 15, 7);

        warehouse.receiveShipment("P001", 5);

        Runnable orderTask1 = () -> warehouse.fulfillOrder("P001", 4);
        Runnable orderTask2 = () -> warehouse.fulfillOrder("P001", 6);
        Runnable orderTask3 = () -> warehouse.fulfillOrder("P002", 10);

        Thread t1 = new Thread(orderTask1);
        Thread t2 = new Thread(orderTask2);
        Thread t3 = new Thread(orderTask3);

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
        }

        warehouse.displayInventory();
    }
}

public class AlertService implements StockObserver {

    @Override
    public void onLowStock(Product product) {
        System.out.println(" Restock Alert: Low stock for " + product.getName() +
                "  only " + product.getQuantity() + " left!");
    }
}

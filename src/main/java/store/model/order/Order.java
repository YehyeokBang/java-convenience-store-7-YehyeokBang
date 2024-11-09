package store.model.order;

public class Order {

    private final ProductName productName;
    private final Quantity quantity;

    public Order(String productName, int quantity) {
        this.productName = new ProductName(productName);
        this.quantity = new Quantity(quantity);
    }

    public String getName() {
        return productName.get();
    }

    public int getQuantity() {
        return quantity.get();
    }
}

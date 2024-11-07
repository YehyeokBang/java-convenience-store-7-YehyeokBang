package store.model;

public class OrderItem {

    private final String name;
    private final int quantity;

    public OrderItem(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}

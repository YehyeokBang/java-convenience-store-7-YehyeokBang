package store.model.order;

import java.util.Objects;

public class Order {

    private final ProductName productName;
    private final OrderQuantity quantity;

    public Order(String productName, int quantity) {
        this.productName = new ProductName(productName);
        this.quantity = new OrderQuantity(quantity);
    }

    public String getName() {
        return productName.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(productName.get(), order.productName.get());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productName.get());
    }
}

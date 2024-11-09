package store.model.order;

import static store.message.InputErrorMessage.MIN_ORDER_QUANTITY;

public class Quantity {

    private static final int MINIMUM_ORDER_QUANTITY = 1;

    private final int value;

    public Quantity(int value) {
        validateQuantity(value);
        this.value = value;
    }

    private void validateQuantity(int quantity) {
        if (quantity < MINIMUM_ORDER_QUANTITY) {
            throw new IllegalArgumentException(MIN_ORDER_QUANTITY.get());
        }
    }

    public int get() {
        return value;
    }
}

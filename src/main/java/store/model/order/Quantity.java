package store.model.order;

import static store.message.InputErrorMessage.INVALID_FORMAT;

public class Quantity {

    private static final int MINIMUM_ORDER_QUANTITY = 1;

    private final int value;

    public Quantity(int value) {
        validateQuantity(value);
        this.value = value;
    }

    private void validateQuantity(int quantity) {
        if (quantity < MINIMUM_ORDER_QUANTITY) {
            throw new IllegalArgumentException(INVALID_FORMAT.get());
        }
    }

    public int get() {
        return value;
    }
}

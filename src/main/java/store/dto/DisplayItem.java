package store.dto;

import store.model.Item;

public record DisplayItem(
        String name,
        int price,
        int quantity,
        String promotion
) {
    public static DisplayItem mapFromItem(Item item) {
        return new DisplayItem(
                item.getName(),
                item.getPrice(),
                item.getQuantity(),
                item.getPromotion()
        );
    }
}

package store.dto;

import store.model.data.product.ProductData;

public record DisplayItem(
        String name,
        int price,
        int quantity,
        String promotion
) {
    public static DisplayItem mapFromItem(ProductData productData) {
        return new DisplayItem(
                productData.name(),
                productData.price(),
                productData.quantity(),
                productData.promotion()
        );
    }
}

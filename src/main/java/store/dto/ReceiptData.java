package store.dto;

import java.util.List;

public record ReceiptData(
        List<ProductInfo> products,
        List<ProductInfo> promotionProducts,
        int totalQuantity,
        int totalPrice,
        int discountPrice,
        int membershipPrice,
        int finalPrice
) {

}


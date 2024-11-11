package store.dto;

import store.model.event.Promotion;

public record PriceTag(
        String productName,
        int price,
        Promotion promotion
) {
    
}

package store.dto;

public record ProductDetails(
        String name,
        int price,
        int quantity,
        String promotion
) {

}

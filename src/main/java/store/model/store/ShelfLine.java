package store.model.store;

import static store.message.InputErrorMessage.EXCEEDS_STOCK;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import store.dto.PriceTag;
import store.model.event.Promotion;

public class ShelfLine {

    private final Deque<Product> products;
    private final PriceTag priceTag;

    public ShelfLine(Deque<Product> products, PriceTag priceTag) {
        this.products = products;
        this.priceTag = priceTag;
    }

    public List<Product> takeOut(int quantity) {
        List<Product> takenProducts = new ArrayList<>();
        if (quantity > products.size()) {
            throw new IllegalArgumentException(EXCEEDS_STOCK.get());
        }
        for (int i = 0; i < quantity; i++) {
            takenProducts.add(products.pollFirst());
        }
        return takenProducts;
    }

    public void addLast(Product product) {
        products.addLast(product);
    }

    public Deque<Product> getProducts() {
        return products;
    }

    public String getProductName() {
        return priceTag.productName();
    }

    public Promotion getPromotion() {
        return priceTag.promotion();
    }

    public int getPrice() {
        return priceTag.price();
    }

    public int getCount() {
        return products.size();
    }
}

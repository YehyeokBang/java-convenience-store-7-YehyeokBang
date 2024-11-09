package store.model.store;

import static store.message.InputErrorMessage.EXCEEDS_STOCK;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ShelfLine {

    private final Deque<Product> products;
    private final String productName;
    private final int price;
    private final Promotion promotion;

    public ShelfLine(Deque<Product> products, String productName, int price, Promotion promotion) {
        this.products = products;
        this.productName = productName;
        this.price = price;
        this.promotion = promotion;
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
        return productName;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public int getPrice() {
        return price;
    }
}

package store.model;

import java.util.ArrayList;
import java.util.List;
import store.model.order.Order;
import store.model.store.Product;
import store.model.store.Shelf;

public class Customer {

    private final List<Product> shoppingCart;

    public Customer() {
        shoppingCart = new ArrayList<>();
    }

    public void addProductsInShoppingCart(Shelf shelf, Order order) {
        List<Product> takenProducts = shelf.takeOut(order.getName(), order.getQuantity());
        shoppingCart.addAll(takenProducts);
    }

    public List<Product> unloadShoppingCart() {
        List<Product> products = new ArrayList<>(shoppingCart);
        shoppingCart.clear();
        return products;
    }
}

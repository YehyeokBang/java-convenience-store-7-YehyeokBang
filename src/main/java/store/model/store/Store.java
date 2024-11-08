package store.model.store;

import java.util.ArrayList;
import java.util.List;
import store.dto.DisplayProduct;
import store.model.OrderItem;
import store.model.data.StoreDataProvider;
import store.model.data.StoreProductManager;
import store.model.data.product.ProductData;
import store.model.data.product.ProductsDataProvider;
import store.model.data.promotion.PromotionData;
import store.model.data.promotion.PromotionsDataProvider;

public class Store {

    private static final String PROMOTIONS_FILE_URL = "src/main/resources/promotions.md";
    private static final String PRODUCTS_FILE_URL = "src/main/resources/products.md";

    private final Shelf shelf;

    private Store(Shelf shelf) {
        this.shelf = shelf;
    }

    public static Store open() {
        return new Store(new Shelf(getProducts()));
    }

    private static List<ShelfLine> getProducts() {
        StoreProductManager storeProductManager = hireStoreManager();
        return storeProductManager.getProducts();
    }

    private static StoreProductManager hireStoreManager() {
        StoreDataProvider<PromotionData> promotionDataProvider = new PromotionsDataProvider(PROMOTIONS_FILE_URL);
        StoreDataProvider<ProductData> productDataProvider = new ProductsDataProvider(PRODUCTS_FILE_URL);
        return new StoreProductManager(promotionDataProvider, productDataProvider);
    }

    public List<DisplayProduct> getInfo() {
        return shelf.getInfo();
    }

    public void purchaseProduct(List<OrderItem> orders) {
        List<Product> cart = new ArrayList<>();
        for (OrderItem order : orders) {
            cart.addAll(shelf.takeOut(order.getName(), order.getQuantity()));
        }
    }
}

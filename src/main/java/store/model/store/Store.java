package store.model.store;

import java.util.ArrayList;
import java.util.List;
import store.dto.DisplayProduct;
import store.model.order.Order;
import store.model.data.StoreDataProvider;
import store.model.data.StoreProductManager;
import store.model.data.product.ProductData;
import store.model.data.product.ProductsDataProvider;
import store.model.data.promotion.PromotionData;
import store.model.data.promotion.PromotionsDataProvider;
import store.model.order.parser.ConsoleOrderParser;

public class Store {

    private static final String PROMOTIONS_FILE_URL = "src/main/resources/promotions.md";
    private static final String PRODUCTS_FILE_URL = "src/main/resources/products.md";

    private final StoreStaff storeStaff;
    private final Shelf shelf;

    private Store(StoreStaff storeStaff, Shelf shelf) {
        this.storeStaff = storeStaff;
        this.shelf = shelf;
    }

    public static Store open() {
        return new Store(new StoreStaff(new ConsoleOrderParser()), new Shelf(getProducts()));
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

    public List<Product> purchaseProduct(String orderMessage) {
        List<Order> orders = storeStaff.order(orderMessage);
        return takeOutProducts(orders);
    }

    private List<Product> takeOutProducts(List<Order> orders) {
        List<Product> cart = new ArrayList<>();
        for (Order order : orders) {
            List<Product> takenProducts = shelf.takeOut(order.getName(), order.getQuantity());
            cart.addAll(takenProducts);
            int count = checkPromotion(takenProducts);
            if (count == 0) {
                continue;
            }
            if (count > 0) {
                cart.addAll(shelf.takeOut(order.getName(), 1));
                continue;
            }
            for (int i = count; i < 0; i++) {
                shelf.add(cart.removeLast());
            }
        }
        return cart;
    }

    private int checkPromotion(List<Product> takenProducts) {
        return storeStaff.checkPromotion(takenProducts);
    }
}

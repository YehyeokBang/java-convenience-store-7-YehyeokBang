package store.model.store;

import java.util.List;
import store.dto.DisplayProduct;
import store.data.StoreDataProvider;
import store.data.StoreProductManager;
import store.data.product.ProductData;
import store.data.product.ProductsDataProvider;
import store.data.promotion.PromotionData;
import store.data.promotion.PromotionsDataProvider;

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
        return new Store(hireStoreStaff(), initShelf());
    }

    private static StoreStaff hireStoreStaff() {
        return new StoreStaff();
    }

    private static Shelf initShelf() {
        return new Shelf(getProducts());
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

    public StoreStaff getStoreStaff() {
        return storeStaff;
    }

    public Shelf getShelf() {
        return shelf;
    }
}

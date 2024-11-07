package store.model;

import java.util.List;
import store.external.ProductsFileReader;
import store.external.PromotionsFileReader;

public class StoreManager {

    private static final String PROMOTIONS_FILE_URL = "src/main/resources/promotion.md";
    private static final String PRODUCTS_FILE_URL = "src/main/resources/products.md";

    public List<Promotion> getPromotions() {
        return new PromotionsFileReader(PROMOTIONS_FILE_URL).readAll();
    }

    public List<Item> getItems() {
        return new ProductsFileReader(PRODUCTS_FILE_URL).readAll();
    }
}

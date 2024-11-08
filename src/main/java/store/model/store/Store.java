package store.model.store;

import java.util.ArrayList;
import java.util.List;
import store.dto.DisplayProduct;
import store.model.OrderItem;
import store.model.YesOrNoParser;
import store.model.data.StoreDataProvider;
import store.model.data.StoreProductManager;
import store.model.data.product.ProductData;
import store.model.data.product.ProductsDataProvider;
import store.model.data.promotion.PromotionData;
import store.model.data.promotion.PromotionsDataProvider;
import store.view.InputView;

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

    public List<Product> purchaseProduct(List<OrderItem> orders) {
        List<Product> cart = new ArrayList<>();
        for (OrderItem order : orders) {
            List<Product> takenProducts = shelf.takeOut(order.getName(), order.getQuantity());
            int count = checkPromotion(takenProducts);
            if (count == 0) {
                cart.addAll(takenProducts);
                continue;
            }
            if (count > 0) {
                cart.addAll(shelf.takeOut(order.getName(), 1));
                continue;
            }
            for (int i = count; i < 0; i++) {
                shelf.add(takenProducts.removeLast());
            }
        }
        return cart;
    }

    private int checkPromotion(List<Product> takenProducts) {
        List<Product> promotionProducts = takenProducts.stream()
                .filter(Product::hasPromotion)
                .toList();

        if (promotionProducts.isEmpty()) {
            return 0;
        }
        Promotion promotion = promotionProducts.getFirst().getPromotion();
        if (!promotion.isValid() || !promotion.isApplicableToday()) {
            return 0;
        }

        int buyQuantity = promotion.getBuyQuantity();
        int freeQuantity = promotion.getGetQuantity();

        int totalPromotionQuantity = promotionProducts.size();
        int remainingQuantity = totalPromotionQuantity % (buyQuantity + freeQuantity);

        if (remainingQuantity >= buyQuantity) {
            int additionalQuantityNeeded = buyQuantity + freeQuantity - remainingQuantity;
            String rawInputNoPromotion = new InputView().requestFreePromotion(
                    promotionProducts.getFirst().getName(),
                    additionalQuantityNeeded
            );
            if (new YesOrNoParser().parse(rawInputNoPromotion)) {
                return 1;
            }
            return 0;
        }

        String productName = promotionProducts.getFirst().getName();
        int normalProductsCount = getNormalProductsCount(takenProducts);
        int quantityNotApplied = buyQuantity - remainingQuantity + normalProductsCount;

        String rawInputNoPromotion = new InputView().requestNoPromotion(productName, quantityNotApplied);
        if (new YesOrNoParser().parse(rawInputNoPromotion)) {
            return 0;
        }
        return -quantityNotApplied;
    }

    private int getNormalProductsCount(List<Product> takenProducts) {
        return (int) takenProducts.stream()
                .filter(product -> !product.hasPromotion())
                .count();
    }
}

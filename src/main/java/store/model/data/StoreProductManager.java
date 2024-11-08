package store.model.data;

import java.util.List;
import store.model.data.product.ProductData;
import store.model.data.promotion.PromotionData;
import store.model.store.Product;
import store.model.store.Promotion;

public class StoreProductManager {

    private final StoreDataProvider<PromotionData> promotionDataProvider;
    private final StoreDataProvider<ProductData> productDataProvider;

    public StoreProductManager(StoreDataProvider<PromotionData> promotionDataProvider,
                               StoreDataProvider<ProductData> productDataProvider) {
        this.promotionDataProvider = promotionDataProvider;
        this.productDataProvider = productDataProvider;
    }

    public List<Product> getProducts() {
        return productDataProvider.getAll()
                .stream()
                .map(this::createProduct)
                .toList();
    }

    private Product createProduct(ProductData data) {
        String name = data.name();
        int price = data.price();
        Promotion promotion = getPromotion(data.promotion());
        return new Product(name, price, promotion);
    }

    private Promotion getPromotion(String promotionName) {
        if (promotionName.equals("null")) {
            return Promotion.getNoPromotion();
        }
        PromotionData promotionData = findByName(promotionName);
        return createPromotion(promotionData);
    }

    private PromotionData findByName(String promotionName) {
        return promotionDataProvider.getAll()
                .stream()
                .filter(data -> data.name().equals(promotionName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 프로모션입니다."));
    }

    private Promotion createPromotion(PromotionData promotionData) {
        return new Promotion(
                promotionData.name(),
                promotionData.buyQuantity(),
                promotionData.getQuantity(),
                promotionData.startDate(),
                promotionData.startDate()
        );
    }
}

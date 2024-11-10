package store.data;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import store.data.product.ProductData;
import store.data.promotion.PromotionData;
import store.model.store.Product;
import store.model.store.Promotion;
import store.model.store.ShelfLine;

public class StoreProductManager {

    private final StoreDataProvider<PromotionData> promotionDataProvider;
    private final StoreDataProvider<ProductData> productDataProvider;

    public StoreProductManager(StoreDataProvider<PromotionData> promotionDataProvider,
                               StoreDataProvider<ProductData> productDataProvider) {
        this.promotionDataProvider = promotionDataProvider;
        this.productDataProvider = productDataProvider;
    }

    public List<ShelfLine> getProducts() {
        List<ShelfLine> shelfLines = productDataProvider.getAll()
                .stream()
                .map(this::createShelfLine)
                .collect(Collectors.toList());
        addMissingNonPromotedProducts(shelfLines);
        return List.copyOf(shelfLines);
    }

    private ShelfLine createShelfLine(ProductData data) {
        String name = data.name();
        int price = data.price();
        Promotion promotion = getPromotion(data.promotionName());

        Deque<Product> products = new ArrayDeque<>();
        for (int i = 0; i < data.quantity(); i++) {
            products.addLast(new Product(name, price, promotion));
        }
        return new ShelfLine(products, name, price, promotion);
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
                promotionData.endDate()
        );
    }

    private void addMissingNonPromotedProducts(List<ShelfLine> shelfLines) {
        var productsByName = shelfLines.stream()
                .collect(Collectors.groupingBy(ShelfLine::getProductName));

        productsByName.forEach((productName, productLines) -> {
            boolean hasPromotionProduct = productLines.stream()
                    .anyMatch(line -> line.getPromotion().isValid());
            boolean hasNormalProduct = productLines.stream()
                    .anyMatch(line -> !line.getPromotion().isValid());

            if (hasPromotionProduct && !hasNormalProduct) {
                int price = productLines.getFirst().getPrice();
                addNonPromotedProduct(shelfLines, productName, price);
            }
        });
    }

    private void addNonPromotedProduct(List<ShelfLine> shelfLines, String productName, int price) {
        Promotion promotion = Promotion.getNoPromotion();
        Deque<Product> products = new ArrayDeque<>();
        shelfLines.add(new ShelfLine(products, productName, price, promotion));
    }
}

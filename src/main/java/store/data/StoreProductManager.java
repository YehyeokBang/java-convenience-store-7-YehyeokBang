package store.data;

import static store.message.FileErrorMessage.EMPTY_PROMOTION;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import store.data.product.ProductData;
import store.data.promotion.PromotionData;
import store.dto.PriceTag;
import store.model.store.Product;
import store.model.event.Promotion;
import store.model.store.ShelfLine;

public class StoreProductManager {

    public static final String NO_PROMOTION = "null";

    private final StoreDataProvider<PromotionData> promotionDataProvider;
    private final StoreDataProvider<ProductData> productDataProvider;

    public StoreProductManager(StoreDataProvider<PromotionData> promotionDataProvider,
                               StoreDataProvider<ProductData> productDataProvider) {
        this.promotionDataProvider = promotionDataProvider;
        this.productDataProvider = productDataProvider;
    }

    public List<ShelfLine> initShelfLines() {
        List<ShelfLine> initialShelfLines = createInitialShelfLines();
        List<ShelfLine> additionalProducts = addMissingNonPromotedProducts(initialShelfLines);
        return combineProducts(initialShelfLines, additionalProducts);
    }

    private List<ShelfLine> createInitialShelfLines() {
        return productDataProvider.getAll()
                .stream()
                .map(this::createShelfLine)
                .collect(Collectors.toList());
    }

    private ShelfLine createShelfLine(ProductData data) {
        Promotion promotion = createPromotion(data.promotionName());
        return new ShelfLine(
                createProducts(data, promotion),
                createPriceTag(data.name(), data.price(), promotion)
        );
    }

    private Promotion createPromotion(String promotionName) {
        if (promotionName.equals(NO_PROMOTION)) {
            return Promotion.getNoPromotion();
        }
        return createPromotionFromData(findPromotionData(promotionName));
    }

    private PromotionData findPromotionData(String promotionName) {
        return promotionDataProvider.getAll()
                .stream()
                .filter(data -> data.name().equals(promotionName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(EMPTY_PROMOTION.get()));
    }

    private Deque<Product> createProducts(ProductData data, Promotion promotion) {
        return Stream.generate(() -> new Product(data.name(), data.price(), promotion))
                .limit(data.quantity())
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    private PriceTag createPriceTag(String productName, int price, Promotion promotion) {
        return new PriceTag(productName, price, promotion);
    }

    private Promotion createPromotionFromData(PromotionData data) {
        return new Promotion(
                data.name(),
                data.buyQuantity(),
                data.getQuantity(),
                data.startDate(),
                data.endDate()
        );
    }

    private List<ShelfLine> addMissingNonPromotedProducts(List<ShelfLine> initialShelfLines) {
        Map<String, List<ShelfLine>> productsByName = groupProductsByName(initialShelfLines);
        return createMissingNonPromotedProducts(productsByName);
    }

    private Map<String, List<ShelfLine>> groupProductsByName(List<ShelfLine> shelfLines) {
        return shelfLines.stream()
                .collect(Collectors.groupingBy(ShelfLine::getProductName));
    }

    private List<ShelfLine> createMissingNonPromotedProducts(Map<String, List<ShelfLine>> productsByName) {
        return productsByName.entrySet().stream()
                .filter(this::needsNonPromotedProduct)
                .map(this::createNonPromotedProduct)
                .collect(Collectors.toList());
    }

    private boolean needsNonPromotedProduct(Map.Entry<String, List<ShelfLine>> entry) {
        List<ShelfLine> productLines = entry.getValue();
        return hasPromotionProduct(productLines) && !hasNormalProduct(productLines);
    }

    private boolean hasPromotionProduct(List<ShelfLine> productLines) {
        return productLines.stream()
                .anyMatch(line -> line.getPromotion().isValid());
    }

    private boolean hasNormalProduct(List<ShelfLine> productLines) {
        return productLines.stream()
                .anyMatch(line -> !line.getPromotion().isValid());
    }

    private ShelfLine createNonPromotedProduct(Map.Entry<String, List<ShelfLine>> entry) {
        String productName = entry.getKey();
        int price = entry.getValue()
                .getFirst()
                .getPrice();
        return new ShelfLine(new ArrayDeque<>(), createPriceTag(productName, price, Promotion.getNoPromotion()));
    }

    private List<ShelfLine> combineProducts(List<ShelfLine> original, List<ShelfLine> additional) {
        return Stream.concat(original.stream(), additional.stream())
                .toList();
    }
}

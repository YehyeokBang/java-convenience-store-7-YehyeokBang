package store.model.store;

import java.util.List;
import store.model.YesOrNoParser;
import store.model.order.Order;
import store.model.order.parser.OrderParser;
import store.util.RetryHandler;
import store.view.InputView;

public class StoreStaff {

    private static final int NO_PROMOTION = 0;
    private static final int NO_CHANGE = 0;

    private final OrderParser<String> orderParser;

    public StoreStaff(OrderParser<String> orderParser) {
        this.orderParser = orderParser;
    }

    public List<Order> order(String orderMessage) {
        return orderParser.parse(orderMessage);
    }

    public int checkPromotion(List<Product> takenProducts) {
        List<Product> promotionProducts = getValidPromotionProducts(takenProducts);
        if (promotionProducts.isEmpty()) {
            return NO_PROMOTION;
        }
        Promotion promotion = promotionProducts.getFirst().getPromotion();
        int totalPromotionQuantity = promotionProducts.size();
        int remainingQuantity = getRemainingQuantity(totalPromotionQuantity, promotion);
        if (isFullPromotion(remainingQuantity)) {
            return NO_CHANGE;
        }

        if (remainingQuantity >= promotion.getBuyQuantity()) {
            return handleFreePromotion(promotionProducts, promotion, remainingQuantity);
        }
        return handleNormalProductPromotion(takenProducts, promotionProducts, remainingQuantity);
    }

    private List<Product> getValidPromotionProducts(List<Product> products) {
        return products.stream()
                .filter(Product::hasPromotion)
                .filter(this::isValidPromotion)
                .toList();
    }

    private boolean isValidPromotion(Product product) {
        return product.getPromotion().isValid();
    }

    private int getRemainingQuantity(int totalPromotionQuantity, Promotion promotion) {
        return totalPromotionQuantity % (promotion.getBuyQuantity() + promotion.getFreeQuantity());
    }

    private boolean isFullPromotion(int remainingQuantity) {
        return remainingQuantity == 0;
    }

    private int handleFreePromotion(List<Product> promotionProducts, Promotion promotion, int remainingQuantity) {
        int additionalQuantityNeeded = calculateAdditionalQuantityNeeded(promotion, remainingQuantity);
        return RetryHandler.retryIfError(() -> requestFreeGet(promotionProducts, additionalQuantityNeeded));
    }

    private int calculateAdditionalQuantityNeeded(Promotion promotion, int remainingQuantity) {
        return promotion.getBuyQuantity() + promotion.getFreeQuantity() - remainingQuantity;
    }

    private int handleNormalProductPromotion(List<Product> takenProducts, List<Product> promotionProducts, int remainingQuantity) {
        Promotion promotion = promotionProducts.getFirst().getPromotion();
        String productName = promotionProducts.getFirst().getName();
        int normalProductsCount = getNormalProductsCount(takenProducts);
        int quantityNotApplied = promotion.getBuyQuantity() - remainingQuantity + normalProductsCount;
        return RetryHandler.retryIfError(() -> requestNoPromotionProduct(productName, quantityNotApplied));
    }

    private int getNormalProductsCount(List<Product> takenProducts) {
        return (int) takenProducts.stream()
                .filter(product -> !product.hasPromotion())
                .count();
    }

    private int requestFreeGet(List<Product> promotionProducts, int additionalQuantityNeeded) {
        String rawInputNoPromotion = new InputView().requestFreePromotion(
                promotionProducts.getFirst().getName(),
                additionalQuantityNeeded
        );
        if (new YesOrNoParser().parse(rawInputNoPromotion)) {
            return 1;
        }
        return 0;
    }

    private int requestNoPromotionProduct(String productName, int quantityNotApplied) {
        String rawInputNoPromotion = new InputView().requestNoPromotion(productName, quantityNotApplied);
        if (new YesOrNoParser().parse(rawInputNoPromotion)) {
            return 0;
        }
        return -quantityNotApplied;
    }
}

package store.model.store;

import java.util.List;

public class StoreStaff {

    public PromotionResult evaluatePromotion(List<Product> takenProducts) {
        List<Product> promotionProducts = filterValidPromotionProducts(takenProducts);
        if (isChoiceAvailableWithPromotion(promotionProducts)) {
            Promotion promotion = promotionProducts.getFirst().getPromotion();
            int remainingQuantity = calculateRemainingQuantityForPromotion(promotionProducts.size(), promotion);
            if (remainingQuantity >= promotion.getBuyQuantity()) {
                return processFreeProductAvailable(promotionProducts, promotion, remainingQuantity);
            }
            return processPromotionInsufficientStock(takenProducts, promotionProducts, remainingQuantity);
        }
        return PromotionResult.createNoChange();
    }

    private boolean isChoiceAvailableWithPromotion(List<Product> promotionProducts) {
        if (promotionProducts.isEmpty()) {
            return false;
        }
        Promotion promotion = promotionProducts.getFirst().getPromotion();
        int remainingQuantity = calculateRemainingQuantityForPromotion(promotionProducts.size(), promotion);
        return !isFullPromotion(remainingQuantity);
    }

    private List<Product> filterValidPromotionProducts(List<Product> products) {
        return products.stream()
                .filter(Product::hasPromotion)
                .filter(this::isPromotionValid)
                .toList();
    }

    private boolean isPromotionValid(Product product) {
        return product.getPromotion().isValid();
    }

    private int calculateRemainingQuantityForPromotion(int totalPromotionQuantity, Promotion promotion) {
        return totalPromotionQuantity % (promotion.getBuyQuantity() + promotion.getFreeQuantity());
    }

    private boolean isFullPromotion(int remainingQuantity) {
        return remainingQuantity == 0;
    }

    private PromotionResult processFreeProductAvailable(List<Product> promotionProducts, Promotion promotion,
                                                        int remainingQuantity) {
        String productName = promotionProducts.getFirst().getName();
        int additionalQuantityNeeded = calculateAdditionalQuantityNeeded(promotion, remainingQuantity);
        return PromotionResult.createFreeProductAvailable(productName, additionalQuantityNeeded);
    }

    private int calculateAdditionalQuantityNeeded(Promotion promotion, int remainingQuantity) {
        return promotion.getBuyQuantity() + promotion.getFreeQuantity() - remainingQuantity;
    }

    private PromotionResult processPromotionInsufficientStock(List<Product> takenProducts,
                                                              List<Product> promotionProducts, int remainingQuantity) {
        Promotion promotion = promotionProducts.getFirst().getPromotion();
        String productName = promotionProducts.getFirst().getName();
        int normalProductsCount = getNormalProductsCount(takenProducts);
        int quantityNotApplied = promotion.getBuyQuantity() - remainingQuantity + normalProductsCount;
        return PromotionResult.createPromotionInsufficientStock(productName, quantityNotApplied);
    }

    private int getNormalProductsCount(List<Product> takenProducts) {
        return (int) takenProducts.stream()
                .filter(product -> !product.hasPromotion())
                .count();
    }
}

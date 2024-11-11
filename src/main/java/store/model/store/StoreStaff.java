package store.model.store;

import java.util.List;
import store.model.event.Promotion;
import store.model.event.PromotionResult;

public class StoreStaff {

    public PromotionResult evaluatePromotion(List<Product> takenProducts, Shelf shelf) {
        List<Product> promotionProducts = filterValidPromotionProducts(takenProducts);
        if (promotionProducts.isEmpty()) {
            return PromotionResult.createNoChange();
        }
        Promotion promotion = promotionProducts.getFirst().getPromotion();
        int totalQuantity = promotionProducts.size();
        if (isFullPromotionApplicable(totalQuantity, promotion) || totalQuantity < promotion.getBuyQuantity()) {
            return PromotionResult.createNoChange();
        }
        return evaluatePromotionResult(takenProducts, shelf, totalQuantity, promotion, promotionProducts);
    }

    private PromotionResult evaluatePromotionResult(List<Product> takenProducts, Shelf shelf, int totalQuantity,
                                                    Promotion promotion, List<Product> promotionProducts) {
        int remainingQuantity = calculateRemainingQuantityForPromotion(totalQuantity, promotion);
        if (remainingQuantity >= promotion.getBuyQuantity()) {
            return processFreeProductAvailable(takenProducts, promotionProducts, promotion, remainingQuantity, shelf);
        }
        return processPromotionInsufficientStock(takenProducts, promotionProducts, remainingQuantity);
    }

    private boolean isFullPromotionApplicable(int totalQuantity, Promotion promotion) {
        int promotionUnit = promotion.getBuyQuantity() + promotion.getFreeQuantity();
        return totalQuantity % promotionUnit == 0;
    }

    private List<Product> filterValidPromotionProducts(List<Product> products) {
        return products.stream()
                .filter(Product::hasPromotion)
                .filter(this::isPromotionValid)
                .toList();
    }

    private boolean isPromotionValid(Product product) {
        return product.getPromotion().isApplicableToday();
    }

    private int calculateRemainingQuantityForPromotion(int totalPromotionQuantity, Promotion promotion) {
        return totalPromotionQuantity % (promotion.getBuyQuantity() + promotion.getFreeQuantity());
    }

    private PromotionResult processFreeProductAvailable(List<Product> takenProducts, List<Product> promotionProducts,
                                                        Promotion promotion, int remainingQuantity, Shelf shelf) {
        String productName = promotionProducts.getFirst().getName();
        int additionalQuantityNeeded = calculateAdditionalQuantityNeeded(promotion, remainingQuantity);
        if (isPromotionStockInsufficient(shelf, productName, additionalQuantityNeeded)) {
            return processPromotionInsufficientStock(takenProducts, promotionProducts, remainingQuantity);
        }
        return PromotionResult.createFreeProductAvailable(productName, additionalQuantityNeeded);
    }

    private boolean isPromotionStockInsufficient(Shelf shelf, String productName, int additionalQuantityNeeded) {
        return shelf.countPromotionProductByName(productName) < additionalQuantityNeeded;
    }

    private int calculateAdditionalQuantityNeeded(Promotion promotion, int remainingQuantity) {
        return promotion.getBuyQuantity() + promotion.getFreeQuantity() - remainingQuantity;
    }

    private PromotionResult processPromotionInsufficientStock(List<Product> takenProducts,
                                                              List<Product> promotionProducts, int remainingQuantity) {
        String productName = promotionProducts.getFirst().getName();
        int normalProductsCount = getNormalProductsCount(takenProducts);
        int quantityNotApplied = remainingQuantity + normalProductsCount;
        return PromotionResult.createPromotionInsufficientStock(productName, quantityNotApplied);
    }

    private int getNormalProductsCount(List<Product> takenProducts) {
        return (int) takenProducts.stream()
                .filter(product -> !product.hasPromotion())
                .count();
    }
}

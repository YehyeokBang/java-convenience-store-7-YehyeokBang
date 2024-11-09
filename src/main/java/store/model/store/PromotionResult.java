package store.model.store;

public class PromotionResult {

    private static final String NO_PRODUCT_NAME = "";

    private final PromotionResultType resultType;
    private final String productName;
    private final int changeCount;

    public PromotionResult(PromotionResultType resultType, String productName, int changeCount) {
        this.resultType = resultType;
        this.productName = productName;
        this.changeCount = changeCount;
    }

    public static PromotionResult createNoChange() {
        return new PromotionResult(PromotionResultType.NO_CHANGE, NO_PRODUCT_NAME, 0);
    }

    public static PromotionResult createFreeProductAvailable(String productName, int count) {
        return new PromotionResult(PromotionResultType.FREE_PRODUCT_AVAILABLE, productName, count);
    }

    public static PromotionResult createPromotionInsufficientStock(String productName, int count) {
        return new PromotionResult(PromotionResultType.PROMOTION_INSUFFICIENT_STOCK, productName, count);
    }

    public boolean checkType(PromotionResultType promotionResultType) {
        return this.resultType == promotionResultType;
    }

    public String getProductName() {
        return productName;
    }

    public int getChangeCount() {
        return changeCount;
    }
}

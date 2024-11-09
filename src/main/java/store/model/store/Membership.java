package store.model.store;

public class Membership {

    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_DISCOUNT_AMOUNT = 8_000;
    private static final int MIN_TOTAL_PRICE_FOR_DISCOUNT = 10_000;
    private static final int NO_DISCOUNT = 0;

    public int getMembershipDiscountPrice(boolean isMembership, int totalPrice) {
        if (isPossibleDiscount(isMembership, totalPrice)) {
            int membershipDiscountPrice = calculateDiscountAmount(totalPrice);
            return ensureMinimumPayment(totalPrice, membershipDiscountPrice);
        }
        return NO_DISCOUNT;
    }

    private boolean isPossibleDiscount(boolean isMembership, int totalPrice) {
        return isMembership && totalPrice > MIN_TOTAL_PRICE_FOR_DISCOUNT;
    }

    private int calculateDiscountAmount(int totalPrice) {
        int discountAmount = (int) (totalPrice * MEMBERSHIP_DISCOUNT_RATE);
        return Math.min(discountAmount, MAX_DISCOUNT_AMOUNT);
    }

    private int ensureMinimumPayment(int totalPrice, int discountAmount) {
        int finalPriceAfterDiscount = totalPrice - discountAmount;
        if (finalPriceAfterDiscount < MIN_TOTAL_PRICE_FOR_DISCOUNT) {
            return totalPrice - MIN_TOTAL_PRICE_FOR_DISCOUNT;
        }
        return discountAmount;
    }
}

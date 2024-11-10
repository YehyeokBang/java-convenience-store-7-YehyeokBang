package store.model.store;

public class Membership {

    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_DISCOUNT_AMOUNT = 8_000;
    private static final int NO_DISCOUNT = 0;

    public int getMembershipDiscountPrice(boolean isMembership, int appliedAmount) {
        if (isPossibleDiscount(isMembership)) {
            return calculateDiscountAmount(appliedAmount);
        }
        return NO_DISCOUNT;
    }

    private boolean isPossibleDiscount(boolean isMembership) {
        return isMembership;
    }

    private int calculateDiscountAmount(int totalPrice) {
        int discountAmount = (int) (totalPrice * MEMBERSHIP_DISCOUNT_RATE);
        return Math.min(discountAmount, MAX_DISCOUNT_AMOUNT);
    }
}

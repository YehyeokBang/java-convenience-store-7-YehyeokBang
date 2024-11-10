package store.model.store;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("멤버십 할인 객체 테스트")
class MembershipTest {

    @DisplayName("멤버십 할인을 선택하지 않은 경우, 멤버십 할인 금액은 0원이다.")
    @ParameterizedTest(name = "멤버십: X, 총구매액: {0}원")
    @ValueSource(ints = {0, 1_000, 10_000})
    void shouldReturnZero_WhenMembershipNotSelected(int totalPrice) {
        Membership membership = new Membership();
        int promotionDiscount = 0;

        int membershipDiscountPrice = membership.getMembershipDiscountPrice(false, totalPrice, promotionDiscount);
        assertEquals(0, membershipDiscountPrice);
    }

    @DisplayName("멤버십 할인을 선택하였으나, 총구매액이 10,000원 이하인 경우 멤버십 할인 금액은 0원이다.")
    @ParameterizedTest(name = "멤버십: O, 총구매액: {0}원")
    @ValueSource(ints = {0, 1_000, 10_000})
    void shouldReturnZero_WhenTotalPriceIsBelowMinimumForDiscount(int totalPrice) {
        Membership membership = new Membership();
        int promotionDiscount = 0;

        int membershipDiscountPrice = membership.getMembershipDiscountPrice(true, totalPrice, promotionDiscount);
        assertEquals(0, membershipDiscountPrice);
    }

    @DisplayName("멤버십 할인을 적용한 후 총 지불액이 최소 지불 금액인 10,000원을 넘지 않도록 조정된다.")
    @ParameterizedTest(name = "멤버십: O, 총구매액: {0}원, 할인 적용 후 지불액: {1}원")
    @CsvSource({"11_000, 10_000", "13_000, 10_000", "15_000, 10_500"})
    void shouldAdjustDiscountToMaintainMinimumTotalPayment(int totalPrice, int expectedFinalPriceAfterDiscount) {
        Membership membership = new Membership();
        int promotionDiscount = 0;

        int membershipDiscountPrice = membership.getMembershipDiscountPrice(true, totalPrice, promotionDiscount);
        int finalPriceAfterDiscount = totalPrice - membershipDiscountPrice;
        assertEquals(expectedFinalPriceAfterDiscount, finalPriceAfterDiscount);
    }

    @DisplayName("멤버십 할인을 적용한 후 총 지불액이 최소 지불 금액인 10,000원을 넘지 않도록 조정된다. (프로모션 O)")
    @ParameterizedTest(name = "멤버십: O, 총구매액: {0}원, 프로모션 적용금액: {1}원, 할인 적용 후 지불액: {2}원")
    @CsvSource({"13_000, 1_000, 10_000"})
    void shouldAdjustDiscountToMaintainMinimumTotalPayment(int totalPrice, int promotionDiscountAmount, int expectedFinalPriceAfterDiscount) {
        Membership membership = new Membership();

        int membershipDiscountPrice = membership.getMembershipDiscountPrice(true, totalPrice, promotionDiscountAmount);
        int finalPriceAfterDiscount = totalPrice - membershipDiscountPrice;
        assertEquals(expectedFinalPriceAfterDiscount, finalPriceAfterDiscount);
    }
}

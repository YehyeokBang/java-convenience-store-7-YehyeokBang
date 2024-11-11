package store.model.event;

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

        int membershipDiscountPrice = membership.getMembershipDiscountPrice(false, totalPrice);
        assertEquals(0, membershipDiscountPrice);
    }

    @DisplayName("멤버십 할인이 적용된다. (프로모션 적용 상품 없음)")
    @ParameterizedTest(name = "멤버십: O, 총구매액: {0}원, 할인금액: {1}원")
    @CsvSource({"10_000, 3_000", "20_000, 6_000", "1_000_000, 8_000"})
    void shouldReturnZero_WhenTotalPriceIsBelowMinimumForDiscount(int totalPrice, int discountAmount) {
        Membership membership = new Membership();

        int membershipDiscountPrice = membership.getMembershipDiscountPrice(true, totalPrice);
        assertEquals(discountAmount, membershipDiscountPrice);
    }

    @DisplayName("멤버십 할인이 적용된다. (프로모션 적용 상품 있음)")
    @ParameterizedTest(name = "멤버십: O, 프로모션 금액: {0}원, 총구매액: {1}원, 할인금액: {2}원")
    @CsvSource({"3_000, 13_000, 3_000", "2_000, 2_000, 0", "0, 100_000, 8_000"})
    void shouldAdjustDiscountToMaintainMinimumTotalPayment(int promotionAmount, int totalPrice, int discountAmount) {
        Membership membership = new Membership();

        int membershipDiscountPrice = membership.getMembershipDiscountPrice(true, totalPrice - promotionAmount);
        assertEquals(discountAmount, membershipDiscountPrice);
    }
}

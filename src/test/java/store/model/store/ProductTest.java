package store.model.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.model.event.Promotion;

@DisplayName("상품 객체 테스트")
class ProductTest {

    @DisplayName("정상적으로 상품 객체가 생성된다.")
    @Test
    void shouldReturnProductObject_WhenIsValid() {
        Promotion everyDayPromotion = new Promotion("평생할인", 2, 1, LocalDate.MIN, LocalDate.MAX);

        assertDoesNotThrow(() -> {
            Product product = new Product("콜라", 1_000, everyDayPromotion);
            assertThat(product.getPrice() == 1_000);
            assertThat(product.getName().equals("콜라"));
            assertThat(product.getPromotion() == everyDayPromotion);
        });
    }

    @DisplayName("프로모션이 적용된 상품인지 확인할 수 있다.")
    @Test
    void possibleCheck_HasPromotion() {
        Promotion everyDayPromotion = new Promotion("평생할인", 2, 1, LocalDate.MIN, LocalDate.MAX);

        assertDoesNotThrow(() -> {
            Product product = new Product("콜라", 1_000, everyDayPromotion);
            boolean hasPromotion = product.hasPromotion();
            assertThat(hasPromotion);
        });
    }

    @DisplayName("프로모션이 적용된 상품이 아닌데 상품의 프로모션을 찾는 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenNoPromotionFind() {
        Product product = new Product("콜라", 1_000, Promotion.getNoPromotion());

        assertThatThrownBy(product::getPromotion)
                .isInstanceOf(IllegalStateException.class);
    }
}

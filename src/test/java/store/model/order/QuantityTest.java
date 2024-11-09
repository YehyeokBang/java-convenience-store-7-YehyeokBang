package store.model.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("상품 수량 객체 테스트")
class QuantityTest {

    @DisplayName("1 이상의 수가 입력되는 경우 정상적으로 객체가 반환된다.")
    @ParameterizedTest(name = "수량: {0}")
    @ValueSource(ints = {1, 10})
    void shouldReturnQuantityObject_WhenInputValueIsValid(int value) {
        assertDoesNotThrow(() -> {
                Quantity quantity = new Quantity(value);
                assertThat(quantity.get() == value);
        });
    }

    @DisplayName("1보다 작은 수가 입력되는 경우 오류가 발생한다.")
    @ParameterizedTest(name = "수량: {0}")
    @ValueSource(ints = {-1, 0})
    void shouldThrowException_WhenInputValueLessThan1(int value) {
        assertThatThrownBy(() -> new Quantity(value))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

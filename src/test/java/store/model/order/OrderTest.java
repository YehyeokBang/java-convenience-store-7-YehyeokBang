package store.model.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("주문 객체 테스트")
class OrderTest {

    @DisplayName("정상적인 입력인 경우 정상적으로 객체가 반환된다.")
    @ParameterizedTest(name = "상품명: {0}, 수량: {1}")
    @CsvSource({"콜라, 5", "오렌지주스, 1", "물, 10"})
    void shouldReturnOrderObject_WhenInputValueIsValid(String name, int quantity) {
        assertDoesNotThrow(() -> {
            Order order = new Order(name, quantity);
            assertThat(order.getName().equals(name));
            assertThat(order.getQuantity() == quantity);
        });
    }

    @DisplayName("상품명이 빈 문자열인 경우 예외가 발생한다.")
    @ParameterizedTest(name = "상품명: {0}, 수량: {1}")
    @CsvSource({" , 5", "\n, 1", "\t, 10"})
    void shouldThrowException_WhenInputProductNameIsEmpty(String name, int quantity) {
        assertThatThrownBy(() -> new Order(name, quantity))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 수량이 1보다 작은 경우 예외가 발생한다.")
    @ParameterizedTest(name = "상품명: {0}, 수량: {1}")
    @CsvSource({"콜라, 0", "오렌지주스, -1", "물, -10"})
    void shouldThrowException_WhenInputQuantityLessThan1(String name, int quantity) {
        assertThatThrownBy(() -> new Order(name, quantity))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

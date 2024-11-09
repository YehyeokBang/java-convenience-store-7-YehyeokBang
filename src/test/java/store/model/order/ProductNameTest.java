package store.model.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("상품명 객체 테스트")
class ProductNameTest {

    @DisplayName("정상적인 입력인 경우 정상적으로 객체가 반환된다.")
    @ParameterizedTest(name = "입력: {0}")
    @ValueSource(strings = {"콜라", "물", "오렌지주스"})
    void shouldReturnQuantityObject_WhenInputValueIsValid(String name) {
        assertDoesNotThrow(() -> {
            ProductName productName = new ProductName(name);
            assertThat(productName.get().equals(name));
        });
    }

    @DisplayName("빈 문자열이 입력되는 경우 오류가 발생한다.")
    @ParameterizedTest(name = "입력: {0}")
    @ValueSource(strings = {"", "\n", "\t"})
    void shouldThrowException_WhenInputProductNameIsEmpty(String productName) {
        assertThatThrownBy(() -> new ProductName(productName))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

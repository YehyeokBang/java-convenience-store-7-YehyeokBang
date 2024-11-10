package store.data.product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 데이터 제공자 객체 테스트")
class ProductsDataProviderTest {

    @DisplayName("데이터 파일 경로가 올바른 경우 정상적으로 작동한다.")
    @Test
    void shouldReturnProductData_WhenFileUrlIsValid() {
        String invalidUrl = "src/main/resources/products.md";
        assertDoesNotThrow(() -> new ProductsDataProvider(invalidUrl).getAll());
    }

    @DisplayName("데이터 파일 경로가 올바르지 않은 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenFileUrlIsInvalid() {
        String invalidUrl = "Invalid URL";
        assertThatThrownBy(() -> new ProductsDataProvider(invalidUrl).getAll())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("[ERROR] 파일(%s)을 불러오는 중 문제가 발생했습니다.", invalidUrl));
    }
}

package store.data.promotion;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("프로모션 데이터 제공자 객체 테스트")
class PromotionsDataProviderTest {

    @DisplayName("데이터 파일 경로가 올바른 경우 정상적으로 작동한다.")
    @Test
    void shouldReturnPromotionData_WhenFileUrlIsValid() {
        String invalidUrl = "src/main/resources/promotions.md";
        assertDoesNotThrow(() -> new PromotionsDataProvider(invalidUrl).getAll());
    }

    @DisplayName("데이터 파일 경로가 올바르지 않은 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenFileUrlIsInvalid() {
        String invalidUrl = "Invalid URL";
        assertThatThrownBy(() -> new PromotionsDataProvider(invalidUrl).getAll())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("[ERROR] 파일(%s)을 불러오는 중 문제가 발생했습니다.", invalidUrl));
    }
}

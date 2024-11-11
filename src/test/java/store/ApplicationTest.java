package store;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends NsTest {

    @DisplayName("파일에 있는 상품 목록을 출력한다.")
    @Test
    void showProductsInFile() {
        assertSimpleTest(() -> {
            run("[물-1]", "N", "N");
            assertThat(output()).contains(
                    "- 콜라 1,000원 10개 탄산2+1",
                    "- 콜라 1,000원 10개",
                    "- 사이다 1,000원 8개 탄산2+1",
                    "- 사이다 1,000원 7개",
                    "- 오렌지주스 1,800원 9개 MD추천상품",
                    "- 오렌지주스 1,800원 재고 없음",
                    "- 탄산수 1,200원 5개 탄산2+1",
                    "- 탄산수 1,200원 재고 없음",
                    "- 물 500원 10개",
                    "- 비타민워터 1,500원 6개",
                    "- 감자칩 1,500원 5개 반짝할인",
                    "- 감자칩 1,500원 5개",
                    "- 초코바 1,200원 5개 MD추천상품",
                    "- 초코바 1,200원 5개",
                    "- 에너지바 2,000원 5개",
                    "- 정식도시락 6,400원 8개",
                    "- 컵라면 1,700원 1개 MD추천상품",
                    "- 컵라면 1,700원 10개"
            );
        });
    }

    @DisplayName("실행 결과 예시 테스트")
    @Test
    void exampleExecutionResult() {
        assertSimpleTest(() -> {
            run("[콜라-3],[에너지바-5]", "Y", "Y",
                    "[콜라-10]", "Y", "N", "Y",
                    "[오렌지주스-1]", "Y", "Y", "N");
            assertThat(output().replaceAll("\\s", "")).contains(
                    "==============W편의점================"
                            + "상품명수량금액콜라33,000에너지바510,000"
                            + "=============증정==============="
                            + "콜라1"
                            + "===================================="
                            + "총구매액813,000행사할인-1,000멤버십할인-3,000내실돈9,000",
                    "==============W편의점================"
                            + "상품명수량금액콜라1010,000"
                            + "=============증정==============="
                            + "콜라2"
                            + "===================================="
                            + "총구매액1010,000행사할인-2,000멤버십할인-0내실돈8,000",
                    "==============W편의점================"
                            + "상품명수량금액오렌지주스23,600"
                            + "=============증정==============="
                            + "오렌지주스1"
                            + "===================================="
                            + "총구매액23,600행사할인-1,800멤버십할인-0내실돈1,800"
            );
        });
    }

    @DisplayName("여러 개의 일반 상품을 구매한다.")
    @Test
    void buyNormalProducts() {
        assertSimpleTest(() -> {
            run("[비타민워터-3],[물-2],[정식도시락-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈18,300");
        });
    }

    @DisplayName("기간에 해당하지 않는 프로모션은 적용되지 않는다.")
    @Test
    void promotionDateIsInvalid() {
        assertNowTest(() -> {
            run("[감자칩-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈3,000");
        }, LocalDate.of(2024, 2, 1).atStartOfDay());
    }

    @DisplayName("기간에 해당하지 않는 프로모션은 적용되지 않는다. (2번 연속 구매)")
    @Test
    void promotionDateIsInvalid_When2buy() {
        assertNowTest(() -> {
            run("[감자칩-2]", "N", "Y", "[감자칩-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈3,000");
        }, LocalDate.of(2024, 2, 1).atStartOfDay());
    }

    @DisplayName("재고가 부족한 경우 예러 메시지가 출력된다.")
    @Test
    void shouldThrowException_WhenInsufficientTotalStock() {
        assertSimpleTest(() -> {
            runException("[컵라면-12]", "N", "N");
            assertThat(output()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        });
    }

    @DisplayName("프로모션 재고가 부족한 경우 일반 금액 결제를 안내한다.")
    @Test
    void shouldThrowException_WhenPromotionStockIsInsufficient() {
        assertSimpleTest(() -> {
            runException("[콜라-11]", "Y", "N");
            assertThat(output()).isNotIn("프로모션 할인이 적용되지 않습니다.");
            assertThat(output()).contains("현재 콜라 2개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈8,000");
        });
    }

    @DisplayName("2+1 상품을 1개만 구매한 경우 아무 안내도 나오지 않는다.")
    @Test
    void shouldNotShowPromotionMessage_WhenOnlyOneItemPurchased() {
        assertSimpleTest(() -> {
            runException("[콜라-1]", "N", "N");
            assertThat(output()).isNotIn("프로모션 할인이 적용되지 않습니다.", "무료로 더 받을 수 있습니다.");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈1,000");
        });
    }

    @DisplayName("2+1 상품을 2개만 구매한 경우 무료로 받을 수 있다는 안내가 출력된다. (추가 구매)")
    @Test
    void shouldShowPromotionMessage_WhenTwoItemsPurchased() {
        assertSimpleTest(() -> {
            runException("[콜라-2]", "Y", "N", "N");
            assertThat(output()).isNotIn("프로모션 할인이 적용되지 않습니다.");
            assertThat(output()).contains("무료로 더 받을 수 있습니다.");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈2,000");
        });
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}

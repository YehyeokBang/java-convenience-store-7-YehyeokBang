package store.model.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.model.order.Order;

@DisplayName("콘솔 주문 파싱 객체 테스트")
class ConsoleOrderParserTest {

    private ConsoleOrderParser orderParser;

    @BeforeEach
    void setUp() {
        orderParser = new ConsoleOrderParser();
    }

    @DisplayName("올바른 입력인 경우 정상적으로 객체가 생성된다. (단일 주문)")
    @ParameterizedTest
    @CsvSource({"[콜라-2],콜라,2", "[물-2],물,2", "[오렌지주스-3],오렌지주스,3"})
    void shouldReturnOrder_WhenInputIsValid(String rawInputOrder, String productName, int quantity) {
        assertDoesNotThrow(() -> {
            List<Order> orders = orderParser.parse(rawInputOrder);
            assertThat(orders.getFirst().getName().equals(productName));
            assertThat(orders.getFirst().getQuantity() == quantity);
        });
    }

    @DisplayName("올바른 입력인 경우 정상적으로 객체가 생성된다. (연속 주문)")
    @ParameterizedTest
    @CsvSource(
            delimiter = '~',
            value = {"[콜라-2],[물-3]~콜라~2~믈~3", "[오렌지주스-5],[컵라면-3]~오렌지주스~5~컵라면~3"})
    void shouldReturnOrders_WhenInputIsValid(String rawInputOrder,
                                             String productName1, int quantity1,
                                             String productName2, int quantity2) {
        assertDoesNotThrow(() -> {
            List<Order> orders = orderParser.parse(rawInputOrder);
            assertThat(orders.getFirst().getName().equals(productName1));
            assertThat(orders.getFirst().getQuantity() == quantity1);
            assertThat(orders.getLast().getName().equals(productName2));
            assertThat(orders.getLast().getQuantity() == quantity2);
        });
    }

    @DisplayName("형식만 갖춘다면 중간 공백이 있어도 예외가 발생하지 않는다.")
    @ParameterizedTest
    @CsvSource({"[콜라 - 2]  , 콜라, 2", "  [물-2]  , 물, 2", "[오렌지 주스 - 3] , 오렌지주스, 3"})
    void shouldNotThrowException_WhenInputIsValidAndContainGap(String rawInputOrder, String productName, int quantity) {
        assertDoesNotThrow(() -> {
            List<Order> orders = orderParser.parse(rawInputOrder);
            assertThat(orders.getFirst().getName().equals(productName));
            assertThat(orders.getFirst().getQuantity() == quantity);
        });
    }

    @DisplayName("대괄호로 감싸지 않은 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"콜라-2", "[콜라-2", "콜라-2]", "(콜라-2)"})
    void shouldThrowException_WhenNoUseBrackets(String rawInputOrder) {
        assertThatThrownBy(() -> orderParser.parse(rawInputOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품명과 주문 수량을 -(하이픈)으로 구분하지 않은 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"[콜라~2]", "[콜라.2]", "[콜라=2]"})
    void shouldThrowException_WhenNoUseHyphen(String rawInputOrder) {
        assertThatThrownBy(() -> orderParser.parse(rawInputOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품명이나 수량에 -(하이픈)을 사용한 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"[콜-라-2]", "[콜라-1-2]"})
    void shouldThrowException_WhenManyHyphen(String rawInputOrder) {
        assertThatThrownBy(() -> orderParser.parse(rawInputOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

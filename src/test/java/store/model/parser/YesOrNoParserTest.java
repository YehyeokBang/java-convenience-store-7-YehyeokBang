package store.model.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static store.message.InputErrorMessage.INVALID_FORMAT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Y/N 파싱 객체 테스트")
class YesOrNoParserTest {

    private YesOrNoParser yesOrNoParser;

    @BeforeEach
    void setUp() {
        yesOrNoParser = new YesOrNoParser();
    }

    @DisplayName("긍정에 해당하는 문자를 입력한 경우 true를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"Y", "y"})
    void shouldReturnTrue_WhenInputIsPositiveCharacter(String input) {
        assertDoesNotThrow(() -> {
            Boolean result = yesOrNoParser.parse(input);
            assertThat(result);
        });
    }

    @DisplayName("부정에 해당하는 문자를 입력한 경우 false를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"N", "n"})
    void shouldReturnFalse_WhenInputIsNegativeCharacter(String input) {
        assertDoesNotThrow(() -> {
            Boolean result = yesOrNoParser.parse(input);
            assertThat(!result);
        });
    }

    @DisplayName("Y/N 형태가 아닌 다른 문자를 입력한 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"yes", "good", "bad", "네"})
    void shouldThrowException_WhenInputIsInvalid(String input) {
        assertThatThrownBy(() -> yesOrNoParser.parse(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_FORMAT.get());
    }
}

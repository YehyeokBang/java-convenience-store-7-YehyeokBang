package store.message;

public enum FileErrorMessage {

    // 상품 파일 관련 에러
    INVALID_INPUT_LENGTH("파일에 값이 비어있는 부분이 존재합니다. 확인해주세요."),
    INVALID_PRICE("파일에 가격이 0 이하인 상품이 존재합니다. 확인해주세요."),
    INVALID_QUANTITY("파일에 수량이 음수인 상품이 존재합니다. 확인해주세요."),
    INVALID_NUMERIC("파일에 숫자 형식이 필요하지만, 아닌 부분이 존재합니다. 확인해주세요."),

    // 프로모션 파일 관련 에러
    EMPTY_PROMOTION_NAME("프로모션 이름이 비어 있습니다."),
    EMPTY_PROMOTION("존재하지 않는 프로모션입니다."),
    INVALID_PROMOTION_DATE_FORMAT("프로모션 날짜 형식이 잘못되었습니다. (올바른 형식: YYYY-MM-DD)"),
    INVALID_PROMOTION_DATE_ORDER("프로모션의 시작 날짜가 종료 날짜보다 늦습니다."),
    INVALID_PROMOTION_GET_AMOUNT("프로모션 '증정 수량'이 1이 아닙니다."),
    INVALID_PROMOTION_BUY_AMOUNT("프로모션 '구매 수량'이 1보다 작습니다."),
    ;

    private static final String MESSAGE_PREFIX = "[ERROR] ";

    private final String message;

    FileErrorMessage(String message) {
        this.message = message;
    }

    public String get() {
        return MESSAGE_PREFIX + message;
    }
}

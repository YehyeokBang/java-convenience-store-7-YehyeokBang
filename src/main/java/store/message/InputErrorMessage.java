package store.message;

public enum InputErrorMessage {

    INVALID_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    NON_EXISTENT_PRODUCT("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    EXCEEDS_STOCK("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INVALID_INPUT("잘못된 입력입니다. 다시 입력해 주세요."),
    ;

    private static final String MESSAGE_PREFIX = "[ERROR] ";

    private final String message;

    InputErrorMessage(String message) {
        this.message = message;
    }

    public String get() {
        return MESSAGE_PREFIX + message;
    }
}

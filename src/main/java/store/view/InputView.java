package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    private static final String REQUEST_PURCHASE_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String REQUEST_MEMBERSHIP_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String REQUEST_REPURCHASE_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public String requestPurchaseItems() {
        System.out.println(REQUEST_PURCHASE_MESSAGE);
        return readInput();
    }

    public String requestMembership() {
        System.out.println(System.lineSeparator() + REQUEST_MEMBERSHIP_MESSAGE);
        return readInput();
    }

    public String requestRepurchase() {
        System.out.println(REQUEST_REPURCHASE_MESSAGE);
        return readInput();
    }

    private String readInput() {
        String rawInput = Console.readLine();
        return rawInput.trim();
    }
}

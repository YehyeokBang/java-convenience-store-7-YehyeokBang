package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    private static final String REQUEST_PURCHASE_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";

    public String requestPurchaseItems() {
        System.out.println(REQUEST_PURCHASE_MESSAGE);
        return readInput();
    }

    private String readInput() {
        String rawInput = Console.readLine();
        return rawInput.trim();
    }
}

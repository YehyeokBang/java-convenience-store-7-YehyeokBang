package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    private static final String REQUEST_ORDER_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String REQUEST_FREE_PROMOTION_MESSAGE = "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    private static final String REQUEST_NO_PROMOTION_MESSAGE = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String REQUEST_MEMBERSHIP_MESSAGE = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String REQUEST_REPURCHASE_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public String requestOrder() {
        System.out.println(System.lineSeparator() + REQUEST_ORDER_MESSAGE);
        return readInput();
    }

    public String requestFreePromotion(String name, int freeQuantity) {
        String message = String.format(REQUEST_FREE_PROMOTION_MESSAGE, name, freeQuantity);
        System.out.println(System.lineSeparator() + message);
        return readInput();
    }

    public String requestNoPromotion(String name, int notApplyQuantity) {
        String message = String.format(REQUEST_NO_PROMOTION_MESSAGE, name, notApplyQuantity);
        System.out.println(System.lineSeparator() + message);
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

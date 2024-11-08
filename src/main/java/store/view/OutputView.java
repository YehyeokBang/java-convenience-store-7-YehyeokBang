package store.view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import store.dto.DisplayProduct;

public class OutputView {

    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String CURRENT_ITEMS_MESSAGE = "현재 보유하고 있는 상품입니다.";

    public void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    public void printCurrentItemsMessage() {
        System.out.println(CURRENT_ITEMS_MESSAGE);
    }

    public void printDisplayItems(List<DisplayProduct> displayProducts) {
        StringBuilder message = new StringBuilder(System.lineSeparator());
        for (DisplayProduct displayProduct : displayProducts) {
            message.append(formatName(displayProduct))
                    .append(formatPrice(displayProduct))
                    .append(formatQuantity(displayProduct))
                    .append(formatPromotion(displayProduct))
                    .append(System.lineSeparator());
        }
        System.out.println(message);
    }

    private String formatPromotion(DisplayProduct displayProduct) {
        if (displayProduct.promotion().equals("null")) {
            return "";
        }
        return displayProduct.promotion();
    }

    private String formatName(DisplayProduct displayProduct) {
        return "- " + displayProduct.name() + " ";
    }

    private String formatPrice(DisplayProduct displayProduct) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        return numberFormat.format(displayProduct.price()) + "원 ";
    }

    private String formatQuantity(DisplayProduct displayProduct) {
        if (displayProduct.quantity() == 0) {
            return "재고 없음 ";
        }
        return displayProduct.quantity() + "개 ";
    }
}

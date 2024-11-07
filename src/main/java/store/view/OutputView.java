package store.view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import store.dto.DisplayItem;

public class OutputView {

    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String CURRENT_ITEMS_MESSAGE = "현재 보유하고 있는 상품입니다.";

    public void printWelcomeMessage() {
        System.out.println(WELCOME_MESSAGE);
    }

    public void printCurrentItemsMessage() {
        System.out.println(CURRENT_ITEMS_MESSAGE);
    }

    public void printDisplayItems(List<DisplayItem> displayItems) {
        StringBuilder message = new StringBuilder(System.lineSeparator());
        for (DisplayItem displayItem : displayItems) {
            message.append(formatName(displayItem))
                    .append(formatPrice(displayItem))
                    .append(formatQuantity(displayItem))
                    .append(formatPromotion(displayItem))
                    .append(System.lineSeparator());
        }
        System.out.println(message);
    }

    private String formatPromotion(DisplayItem displayItem) {
        if (displayItem.promotion().equals("null")) {
            return "";
        }
        return displayItem.promotion();
    }

    private String formatName(DisplayItem displayItem) {
        return displayItem.name() + " - ";
    }

    private String formatPrice(DisplayItem displayItem) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        return numberFormat.format(displayItem.price()) + "원 ";
    }

    private String formatQuantity(DisplayItem displayItem) {
        return displayItem.quantity() + "개 ";
    }
}

package store.view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import store.dto.DisplayProduct;
import store.dto.ProductInfo;
import store.dto.ReceiptData;

public class OutputView {

    private static final String WELCOME_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String CURRENT_ITEMS_MESSAGE = "현재 보유하고 있는 상품입니다.";
    private static final String RECEIPT_HEADER = "===========W 편의점=============\n";
    private static final String PRODUCT_HEADER = String.format("%-8s\t\t%-2s\t\t%s\n", "상품명", "수량", "금액");
    private static final String PROMOTION_HEADER = "===========증\t정=============\n";
    private static final String TOTAL_LABEL = "%-8s\t\t%-2s\t\t%,d\n";
    private static final String DISCOUNT_LABEL = "%-10s\t\t\t-%,d\n";
    private static final String RECEIPT_FOOTER = "==============================\n";

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

    public void printReceipt(ReceiptData receiptData) {
        StringBuilder receipt = new StringBuilder(System.lineSeparator());
        writeReceiptHeader(receiptData, receipt);
        writePromotion(receiptData, receipt);
        writeFooter(receiptData, receipt);
        System.out.println(receipt);
    }

    private void writeReceiptHeader(ReceiptData receiptData, StringBuilder receipt) {
        receipt.append(RECEIPT_HEADER);
        receipt.append(PRODUCT_HEADER);
        for (ProductInfo info : receiptData.products()) {
            receipt.append(String.format("%-8s\t\t%-2d\t\t%,d\n", info.name(), info.quantity(), info.price()));
        }
    }

    private void writePromotion(ReceiptData receiptData, StringBuilder receipt) {
        List<ProductInfo> promotionProducts = receiptData.promotionProducts();
        if (!promotionProducts.isEmpty()) {
            receipt.append(PROMOTION_HEADER);
            for (ProductInfo info : promotionProducts) {
                receipt.append(String.format("%-8s\t\t%-2d\n", info.name(), info.quantity()));
            }
        }
    }

    private void writeFooter(ReceiptData receiptData, StringBuilder receipt) {
        receipt.append(RECEIPT_FOOTER);
        receipt.append(String.format(TOTAL_LABEL, "총구매액", receiptData.totalQuantity(), receiptData.totalPrice()));
        receipt.append(String.format(DISCOUNT_LABEL, "행사할인", receiptData.discountPrice()));
        receipt.append(String.format(DISCOUNT_LABEL, "멤버십할인", receiptData.membershipPrice()));
        receipt.append(String.format(DISCOUNT_LABEL, "내실돈\t", receiptData.finalPrice()));
    }
}

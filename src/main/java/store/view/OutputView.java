package store.view;

import java.util.List;
import store.dto.ProductDetails;
import store.dto.ProductInfo;
import store.dto.ReceiptData;

public class OutputView {

    private static final String WELCOME = "안녕하세요. W편의점입니다.";
    private static final String CURRENT_PRODUCTS = "현재 보유하고 있는 상품입니다.";
    private static final String NO_PROMOTION = "null";
    private static final String NO_STOCK = "재고 없음";
    private static final String HEADER = "==============W 편의점================\n";
    private static final String PRODUCT_HEADER = "%-8s\t\t\t%-2s\t\t%s\n";
    private static final String PROMOTION_HEADER = "=============증\t\t정===============\n";
    private static final String FOOTER = "====================================\n";
    private static final String PRODUCT_LINE = "%-8s\t\t\t%-2d\t\t%,d\n";
    private static final String PROMOTION_LINE = "%-8s\t\t\t%-2d\n";
    private static final String TOTAL_LINE = "%-8s\t\t\t%-2s\t\t%,d\n";
    private static final String DISCOUNT_LINE = "%-10s\t\t\t\t-%,d\n";
    private static final String FINAL_PRICE_LINE = "%-10s\t\t\t\t %,d\n";

    private final ProductFormatter productFormatter;
    private final ReceiptFormatter receiptFormatter;

    public OutputView() {
        this.productFormatter = new ProductFormatter();
        this.receiptFormatter = new ReceiptFormatter();
    }

    public void printWelcomeMessage() {
        println(WELCOME);
    }

    public void printCurrentProductsMessage() {
        println(CURRENT_PRODUCTS + System.lineSeparator());
    }

    public void printProductDetails(List<ProductDetails> products) {
        String formattedProducts = productFormatter.formatProducts(products);
        println(formattedProducts);
    }

    public void printReceipt(ReceiptData receiptData) {
        String formattedReceipt = receiptFormatter.formatReceipt(receiptData);
        println(formattedReceipt);
    }

    private void println(String message) {
        System.out.println(message);
    }

    private static class ProductFormatter {

        private static final String PRODUCT_FORMAT = "- %s %s원 %s";
        private static final String PROMOTION_PRODUCT_FORMAT = "- %s %s원 %s %s";

        String formatProducts(List<ProductDetails> products) {
            return products.stream()
                    .map(this::formatProduct)
                    .reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append)
                    .toString();
        }

        private StringBuilder formatProduct(ProductDetails product) {
            if (isPromotionProduct(product)) {
                String formatted = formatPromotionProduct(product);
                return new StringBuilder(formatted).append(System.lineSeparator());
            }
            String formatted = formatRegularProduct(product);
            return new StringBuilder(formatted).append(System.lineSeparator());
        }

        private String formatRegularProduct(ProductDetails product) {
            return String.format(PRODUCT_FORMAT,
                    product.name(),
                    formatPrice(product.price()),
                    formatQuantity(product.quantity()));
        }

        private String formatPromotionProduct(ProductDetails product) {
            return String.format(PROMOTION_PRODUCT_FORMAT,
                    product.name(),
                    formatPrice(product.price()),
                    formatQuantity(product.quantity()),
                    product.promotion());
        }

        private String formatPrice(int price) {
            return String.format("%,d", price);
        }

        private String formatQuantity(int quantity) {
            if (quantity == 0) {
                return NO_STOCK;
            }
            return quantity + "개";
        }

        private boolean isPromotionProduct(ProductDetails product) {
            return !product.promotion()
                    .equals(NO_PROMOTION);
        }
    }

    private static class ReceiptFormatter {

        String formatReceipt(ReceiptData data) {
            StringBuilder receipt = new StringBuilder(System.lineSeparator());
            appendReceiptHeader(data, receipt);
            appendPromotionSection(data, receipt);
            appendFooter(data, receipt);
            return receipt.toString();
        }

        private void appendReceiptHeader(ReceiptData data, StringBuilder receipt) {
            receipt.append(HEADER)
                    .append(String.format(PRODUCT_HEADER, "상품명", "수량", "금액"));
            for (ProductInfo product : data.products()) {
                String message = String.format(PRODUCT_LINE, product.name(), product.quantity(), product.price());
                receipt.append(message);
            }
        }

        private void appendPromotionSection(ReceiptData data, StringBuilder receipt) {
            List<ProductInfo> promotionProducts = data.promotionProducts();
            if (promotionProducts.isEmpty()) {
                return;
            }
            receipt.append(PROMOTION_HEADER);
            for (ProductInfo product : promotionProducts) {
                String message = String.format(PROMOTION_LINE, product.name(), product.quantity());
                receipt.append(message);
            }
        }

        private void appendFooter(ReceiptData data, StringBuilder receipt) {
            receipt.append(FOOTER)
                    .append(String.format(TOTAL_LINE, "총구매액", data.totalQuantity(), data.totalPrice()))
                    .append(String.format(DISCOUNT_LINE, "행사할인", data.discountPrice()))
                    .append(String.format(DISCOUNT_LINE, "멤버십할인", data.membershipPrice()))
                    .append(String.format(FINAL_PRICE_LINE, "내실돈\t", data.finalPrice()));
        }
    }
}

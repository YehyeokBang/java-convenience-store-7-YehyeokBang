package store.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import store.dto.DisplayProduct;
import store.dto.ProductInfo;
import store.dto.ReceiptData;
import store.model.YesOrNoParser;
import store.model.store.Membership;
import store.model.store.Product;
import store.model.store.Promotion;
import store.model.store.Store;
import store.util.RetryHandler;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final InputView inputView;
    private final OutputView outputView;

    public StoreController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void start() {
        Store store = Store.open();
        boolean isRepurchase;
        do {
            enterStore();
            showItems(store.getInfo());
            List<Product> products = RetryHandler.retryIfError(() -> order(store));
            boolean isMembership = RetryHandler.retryIfError(this::membership);
            ReceiptData receiptData = calculateProducts(products, isMembership);
            printReceipt(receiptData);
            isRepurchase = RetryHandler.retryIfError(this::repurchase);
        } while (isRepurchase);
    }

    private void enterStore() {
        outputView.printWelcomeMessage();
    }

    private void showItems(List<DisplayProduct> products) {
        outputView.printCurrentItemsMessage();
        outputView.printDisplayItems(products);
    }

    private List<Product> order(Store store) {
        String rawInputPurchaseItems = inputView.requestPurchaseItems();
        return store.purchaseProduct(rawInputPurchaseItems);
    }

    private boolean membership() {
        String rawInputMembership = inputView.requestMembership();
        return new YesOrNoParser().parse(rawInputMembership);
    }

    private void printReceipt(ReceiptData receiptData) {
        outputView.printReceipt(receiptData);
    }

    private ReceiptData calculateProducts(List<Product> products, boolean isMembership) {
        Map<String, List<Product>> groupedProducts = products.stream()
                .collect(Collectors.groupingBy(Product::getName));

        List<ProductInfo> productInfos = new ArrayList<>();
        List<ProductInfo> promotionProducts = new ArrayList<>();
        int totalPrice = 0;
        int totalQuantity = 0;
        int promotionDiscount = 0;

        for (Map.Entry<String, List<Product>> entry : groupedProducts.entrySet()) {
            String productName = entry.getKey();
            List<Product> productList = entry.getValue();
            List<Product> promotionProducts2 = productList.stream()
                    .filter(Product::hasPromotion)
                    .toList();
            Product product = productList.getFirst();
            int quantity = productList.size();
            int quantity2 = promotionProducts2.size();
            int price = product.getPrice();

            int promotionQuantity = 0;
            if (product.hasPromotion()) {
                Promotion promotion = product.getPromotion();
                if (promotion.isApplicableToday()) {
                    int buyQuantity = promotion.getBuyQuantity();
                    int freeQuantity = promotion.getFreeQuantity();
                    promotionQuantity = quantity2 / (buyQuantity + freeQuantity);
                }
            }
            int productTotalPrice = price * quantity;
            totalPrice += productTotalPrice;

            productInfos.add(new ProductInfo(productName, price, quantity));
            if (promotionQuantity > 0) {
                promotionDiscount = price * promotionQuantity;
                promotionProducts.add(new ProductInfo(productName, price, promotionQuantity));
            }

            totalQuantity += quantity;
        }

        int membershipDiscountPrice = getMembershipDiscountPrice(isMembership, totalPrice);

        int finalPrice = totalPrice - membershipDiscountPrice - promotionDiscount;
        return new ReceiptData(
                productInfos,
                promotionProducts,
                totalQuantity,
                totalPrice,
                promotionDiscount,
                membershipDiscountPrice,
                finalPrice
        );
    }

    private int getMembershipDiscountPrice(boolean isMembership, int totalPrice) {
        return new Membership().getMembershipDiscountPrice(isMembership, totalPrice);
    }

    private boolean repurchase() {
        String rawInputRepurchase = inputView.requestRepurchase();
        return new YesOrNoParser().parse(rawInputRepurchase);
    }
}

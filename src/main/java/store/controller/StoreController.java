package store.controller;

import java.util.List;
import store.dto.DisplayProduct;
import store.dto.ProductInfo;
import store.dto.ReceiptData;
import store.model.OrderItem;
import store.model.OrderParser;
import store.model.YesOrNoParser;
import store.model.store.Product;
import store.model.store.Store;
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
            List<OrderItem> orders = order();
            List<Product> products = store.purchaseProduct(orders);
            boolean isMembership = membership();
            printTestReceipt();
            isRepurchase = repurchase();
        } while (isRepurchase);
    }

    private void enterStore() {
        outputView.printWelcomeMessage();
    }

    private void showItems(List<DisplayProduct> products) {
        outputView.printCurrentItemsMessage();
        outputView.printDisplayItems(products);
    }

    private List<OrderItem> order() {
        String rawInputPurchaseItems = inputView.requestPurchaseItems();
        OrderParser orderParser = new OrderParser();
        return orderParser.parse(rawInputPurchaseItems);
    }

    private boolean membership() {
        String rawInputMembership = inputView.requestMembership();
        return new YesOrNoParser().parse(rawInputMembership);
    }

    // TODO: 기능 구현 후 제거 필요
    private void printTestReceipt() {
        ReceiptData receiptData = testReceipt();
        outputView.printReceipt(receiptData);
    }

    // TODO: 기능 구현 후 제거 필요
    private ReceiptData testReceipt() {
        List<ProductInfo> products = List.of(
                new ProductInfo("콜라", 3000, 3),
                new ProductInfo("에너지바", 10000, 5)
        );
        List<ProductInfo> promotionProducts = List.of(
                new ProductInfo("콜라", 0, 1)
        );
        return new ReceiptData(
                products,
                promotionProducts,
                8,
                13000,
                1000,
                3000,
                9000
        );
    }

    private boolean repurchase() {
        String rawInputRepurchase = inputView.requestRepurchase();
        return new YesOrNoParser().parse(rawInputRepurchase);
    }
}

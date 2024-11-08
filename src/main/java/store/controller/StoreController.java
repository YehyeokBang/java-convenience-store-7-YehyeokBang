package store.controller;

import java.util.List;
import store.dto.DisplayProduct;
import store.dto.ProductInfo;
import store.dto.ReceiptData;
import store.model.OrderItem;
import store.model.OrderParser;
import store.model.YesOrNoParser;
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
        enterStore();
        showItems(store.getInfo());
        List<OrderItem> orders = order();
        store.purchaseProduct(orders);
        boolean isMembership = membership();
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

    private boolean membership() {
        String rawInputMembership = inputView.requestMembershipMessage();
        return new YesOrNoParser().parse(rawInputMembership);
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
}

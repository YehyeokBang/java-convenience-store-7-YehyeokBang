package store.controller;

import java.util.List;
import store.dto.DisplayProduct;
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

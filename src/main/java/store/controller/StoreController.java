package store.controller;

import java.util.List;
import store.dto.DisplayItem;
import store.model.Item;
import store.model.OrderItem;
import store.model.OrderParser;
import store.model.ProductsManager;
import store.model.PromotionManager;
import store.model.Promotions;
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
        PromotionManager promotionManager = new PromotionManager();
        Promotions promotions = promotionManager.getAllPromotions();
        enterStore();
        List<OrderItem> orders = order();
    }

    private void enterStore() {
        outputView.printWelcomeMessage();
        showItems();
    }

    private void showItems() {
        outputView.printCurrentItemsMessage();
        List<DisplayItem> displayItems = getDisplayItems();
        outputView.printDisplayItems(displayItems);
    }

    private List<DisplayItem> getDisplayItems() {
        List<Item> items = getItems();
        return getDisplayItems(items);
    }

    private List<Item> getItems() {
        ProductsManager productsManager = new ProductsManager();
        return productsManager.getAllItems();
    }

    private List<DisplayItem> getDisplayItems(List<Item> items) {
        return items.stream()
                .map(DisplayItem::mapFromItem)
                .toList();
    }

    private List<OrderItem> order() {
        String rawInputPurchaseItems = inputView.requestPurchaseItems();
        OrderParser orderParser = new OrderParser();
        return orderParser.parse(rawInputPurchaseItems);
    }
}

package store.controller;

import java.util.List;
import store.dto.DisplayItem;
import store.model.Item;
import store.model.StockStorage;
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
        enterStore();
        String rawInputPurchaseItems = inputView.requestPurchaseItems();
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
        StockStorage stockStorage = new StockStorage();
        List<Item> items = stockStorage.getAllItems();
        return getDisplayItems(items);
    }

    private List<DisplayItem> getDisplayItems(List<Item> items) {
        return items.stream()
                .map(DisplayItem::mapFromItem)
                .toList();
    }

}

package store.controller;

import java.util.List;
import store.dto.DisplayItem;
import store.model.Item;
import store.model.StockStorage;
import store.view.OutputView;

public class StoreController {

    private final OutputView outputView;

    public StoreController(OutputView outputView) {
        this.outputView = outputView;
    }

    public void start() {
        outputView.printWelcomeMessage();
        outputView.printCurrentItemsMessage();
        StockStorage stockStorage = new StockStorage();
        List<Item> items = stockStorage.getAllItems();
        List<DisplayItem> displayItems = getDisplayItems(items);
        outputView.printDisplayItems(displayItems);
    }

    private List<DisplayItem> getDisplayItems(List<Item> items) {
        return items.stream()
                .map(DisplayItem::mapFromItem)
                .toList();
    }

}

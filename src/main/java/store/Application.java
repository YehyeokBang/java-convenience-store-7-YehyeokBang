package store;

import store.controller.StoreController;
import store.view.OutputView;

public class Application {

    public static void main(String[] args) {
        StoreController storeController = initController();
        storeController.start();
    }

    private static StoreController initController() {
        OutputView outputView = new OutputView();
        return new StoreController(outputView);
    }
}

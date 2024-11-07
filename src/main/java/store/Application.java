package store;

import store.controller.StoreController;
import store.view.InputView;
import store.view.OutputView;

public class Application {

    public static void main(String[] args) {
        StoreController storeController = initController();
        storeController.start();
    }

    private static StoreController initController() {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        return new StoreController(inputView, outputView);
    }
}

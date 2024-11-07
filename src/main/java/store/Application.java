package store;

import store.view.OutputView;

public class Application {

    public static void main(String[] args) {
        OutputView outputView = new OutputView();
        outputView.printWelcomeMessage();
        outputView.printCurrentItemsMessage();
    }
}

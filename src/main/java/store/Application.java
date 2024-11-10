package store;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.controller.StoreController;
import store.model.order.Order;
import store.model.parser.ConsoleOrderParser;
import store.model.parser.Parser;
import store.view.InputView;
import store.view.OutputView;

public class Application {

    public static void main(String[] args) {
        StoreController storeController = initController();
        storeController.start();
        releaseResources();
    }

    private static StoreController initController() {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        Parser<String, List<Order>> orderParser = new ConsoleOrderParser();
        return new StoreController(inputView, outputView, orderParser);
    }

    private static void releaseResources() {
        Console.close();
    }
}

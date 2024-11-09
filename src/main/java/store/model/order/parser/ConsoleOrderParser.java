package store.model.order.parser;

import static store.message.InputError.INVALID_FORMAT;

import java.util.Arrays;
import java.util.List;
import store.model.order.Order;

public class ConsoleOrderParser implements OrderParser<String> {

    private static final String ORDERS_SEPARATOR = ",";
    private static final String START_BRACKET = "[";
    private static final String END_BRACKET = "]";
    private static final String CONTENT_SEPARATOR = "-";
    private static final int REQUIRED_PARTS_COUNT = 2;

    @Override
    public List<Order> parse(String rawInputOrder) {
        String trimmedInput = removeSpaces(rawInputOrder);
        String[] rawOrders = splitOrder(trimmedInput);
        return Arrays.stream(rawOrders)
                .map(this::parseOrder)
                .toList();
    }

    private String removeSpaces(String input) {
        return input.replaceAll(" ", "");
    }

    private String[] splitOrder(String rawOrderInput) {
        return rawOrderInput.split(ORDERS_SEPARATOR);
    }

    private Order parseOrder(String rawOrder) {
        validateBrackets(rawOrder);
        String content = extractContentInsideBrackets(rawOrder);
        return createOrder(content);
    }

    private void validateBrackets(String order) {
        if (!(order.startsWith(START_BRACKET) && order.endsWith(END_BRACKET))) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }

    private String extractContentInsideBrackets(String order) {
        return order.substring(1, order.length() - 1);
    }

    private Order createOrder(String content) {
        String[] parts = splitOrderContent(content);
        String productName = parts[0];
        String quantity = parts[1];
        validateInteger(quantity);
        return new Order(productName, Integer.parseInt(quantity));
    }

    private String[] splitOrderContent(String content) {
        String[] parts = content.split(CONTENT_SEPARATOR);
        if (parts.length != REQUIRED_PARTS_COUNT) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
        return parts;
    }

    private void validateInteger(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }
}

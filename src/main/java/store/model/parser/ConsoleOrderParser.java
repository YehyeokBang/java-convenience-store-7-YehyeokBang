package store.model.parser;

import static store.message.InputErrorMessage.INVALID_FORMAT;
import static store.message.InputErrorMessage.INVALID_INPUT;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import store.model.order.Order;

public class ConsoleOrderParser implements Parser<String, List<Order>> {

    private static final String ORDERS_SEPARATOR = ",";
    private static final String START_BRACKET = "[";
    private static final String END_BRACKET = "]";
    private static final String CONTENT_SEPARATOR = "-";
    private static final int REQUIRED_PARTS_COUNT = 2;

    @Override
    public List<Order> parse(String rawInputOrder) {
        String trimmedInput = removeSpaces(rawInputOrder);
        String[] rawOrders = splitOrder(trimmedInput);
        List<Order> orders = getOrders(rawOrders);
        validateOrderDuplicate(orders);
        return orders;
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
            throw new IllegalArgumentException(INVALID_FORMAT.get());
        }
    }

    private String extractContentInsideBrackets(String order) {
        return order.substring(1, order.length() - 1);
    }

    private Order createOrder(String content) {
        String[] parts = splitOrderContent(content);
        String productName = parts[0];
        int quantity = parseInt(parts[1]);
        return new Order(productName, quantity);
    }

    private String[] splitOrderContent(String content) {
        String[] parts = content.split(CONTENT_SEPARATOR);
        if (parts.length != REQUIRED_PARTS_COUNT) {
            throw new IllegalArgumentException(INVALID_FORMAT.get());
        }
        return parts;
    }

    private int parseInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_FORMAT.get());
        }
    }

    private List<Order> getOrders(String[] rawOrders) {
        return Arrays.stream(rawOrders)
                .map(this::parseOrder)
                .toList();
    }

    private void validateOrderDuplicate(List<Order> orders) {
        int uniqueSize = new HashSet<>(orders).size();
        if (uniqueSize != orders.size()) {
            throw new IllegalArgumentException(INVALID_INPUT.get());
        }
    }
}

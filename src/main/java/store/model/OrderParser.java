package store.model;

import java.util.Arrays;
import java.util.List;
import org.junit.platform.commons.util.StringUtils;

public class OrderParser {

    public List<OrderItem> parse(String rawOrderInput) {
        String[] rawOrders = splitOrders(rawOrderInput);

        return Arrays.stream(rawOrders)
                .map(this::getOrderItem)
                .toList();
    }

    private String[] splitOrders(String order) {
        return order.split(",");
    }

    private OrderItem getOrderItem(String rawInputOrders) {
        validateBrackets(rawInputOrders);
        String content = extractContentInBrackets(rawInputOrders);
        return createOrderItem(content);
    }

    private void validateBrackets(String order) {
        if (!(order.startsWith("[") && order.endsWith("]"))) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    private String extractContentInBrackets(String order) {
        return order.substring(1, order.length() - 1);
    }

    private OrderItem createOrderItem(String content) {
        String[] parts = splitOrderContent(content);
        String orderItemName = parts[0];
        String orderQuantity = parts[1];
        validateOrderItemName(orderItemName);
        validateOrderQuantity(orderQuantity);
        return new OrderItem(orderItemName, Integer.parseInt(orderQuantity));
    }

    private String[] splitOrderContent(String content) {
        String[] parts = content.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        return parts;
    }

    private void validateOrderItemName(String orderItemName) {
        if (StringUtils.isBlank(orderItemName)) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    private void validateOrderQuantity(String orderQuantity) {
        if (StringUtils.isBlank(orderQuantity)) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
        validateInteger(orderQuantity);
    }

    private void validateInteger(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }
}

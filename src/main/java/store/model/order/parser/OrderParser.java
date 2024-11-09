package store.model.order.parser;

import java.util.List;
import store.model.order.Order;

public interface OrderParser<T> {

    List<Order> parse(T input);
}

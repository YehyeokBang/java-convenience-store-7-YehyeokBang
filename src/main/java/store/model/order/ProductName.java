package store.model.order;

import static store.message.InputErrorMessage.INVALID_FORMAT;

import org.junit.platform.commons.util.StringUtils;

public class ProductName {

    private final String name;

    public ProductName(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException(INVALID_FORMAT.get());
        }
    }

    public String get() {
        return name;
    }
}

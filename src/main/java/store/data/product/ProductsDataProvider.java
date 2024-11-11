package store.data.product;

import static store.data.product.ProductsFormat.NAME;
import static store.data.product.ProductsFormat.PRICE;
import static store.data.product.ProductsFormat.PROMOTION;
import static store.data.product.ProductsFormat.QUANTITY;
import static store.message.FileErrorMessage.INVALID_NUMERIC;
import static store.message.FileErrorMessage.INVALID_PRICE;
import static store.message.FileErrorMessage.INVALID_QUANTITY;
import static store.message.FileErrorMessage.INVALID_INPUT_LENGTH;

import java.util.List;
import org.junit.platform.commons.util.StringUtils;
import store.data.FileReader;
import store.data.StoreDataProvider;

public class ProductsDataProvider extends FileReader<ProductData>
        implements StoreDataProvider<ProductData> {

    private final String fileUrl;

    public ProductsDataProvider(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    protected String getFileAddress() {
        return fileUrl;
    }

    @Override
    protected ProductData parseLine(String line) {
        String[] rowItem = line.split(SEPARATOR);
        String name = rowItem[NAME.getColumnPosition()];
        int price = parseInt(rowItem[PRICE.getColumnPosition()]);
        int quantity = parseInt(rowItem[QUANTITY.getColumnPosition()]);
        String promotionName = rowItem[PROMOTION.getColumnPosition()];
        validate(name, price, quantity, promotionName);
        return new ProductData(name, price, quantity, promotionName);
    }

    @Override
    public List<ProductData> getAll() {
        return readAll();
    }

    private void validate(String name, int price, int quantity, String promotionName) {
        validateInputEmpty(name);
        validatePrice(price);
        validateQuantity(quantity);
        validateInputEmpty(promotionName);
    }

    private void validateInputEmpty(String input) {
        if (StringUtils.isBlank(input)) {
            throw new IllegalStateException(INVALID_INPUT_LENGTH.get());
        }
    }

    private void validatePrice(int price) {
        if (price <= 0) {
            throw new IllegalStateException(INVALID_PRICE.get());
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalStateException(INVALID_QUANTITY.get());
        }
    }

    private int parseInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalStateException(INVALID_NUMERIC.get());
        }
    }
}

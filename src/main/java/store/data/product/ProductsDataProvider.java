package store.data.product;

import static store.data.product.ProductsFormat.NAME;
import static store.data.product.ProductsFormat.PRICE;
import static store.data.product.ProductsFormat.PROMOTION;
import static store.data.product.ProductsFormat.QUANTITY;

import java.util.List;
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
        int price = Integer.parseInt(rowItem[PRICE.getColumnPosition()]);
        int quantity = Integer.parseInt(rowItem[QUANTITY.getColumnPosition()]);
        String promotion = rowItem[PROMOTION.getColumnPosition()];
        return new ProductData(name, price, quantity, promotion);
    }

    @Override
    public List<ProductData> getAll() {
        return readAll();
    }
}

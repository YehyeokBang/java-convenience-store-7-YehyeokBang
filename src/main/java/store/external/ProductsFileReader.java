package store.external;

import static store.external.ProductsFormat.NAME;
import static store.external.ProductsFormat.PRICE;
import static store.external.ProductsFormat.PROMOTION;
import static store.external.ProductsFormat.QUANTITY;

import java.util.List;
import store.model.Item;

public class ProductsFileReader extends FileReader<Item> {

    private final String fileUrl;

    public ProductsFileReader(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    protected String getFileAddress() {
        return fileUrl;
    }

    @Override
    protected Item parseLine(String line) {
        String[] rowItem = line.split(SEPARATOR);
        String name = rowItem[NAME.getColumnPosition()];
        int price = Integer.parseInt(rowItem[PRICE.getColumnPosition()]);
        int quantity = Integer.parseInt(rowItem[QUANTITY.getColumnPosition()]);
        String promotion = rowItem[PROMOTION.getColumnPosition()];
        return new Item(name, price, quantity, promotion);
    }

    public List<Item> getAllItems() {
        return readAll();
    }
}

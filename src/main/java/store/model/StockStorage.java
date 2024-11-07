package store.model;

import static store.model.ProductsFormat.NAME;
import static store.model.ProductsFormat.PRICE;
import static store.model.ProductsFormat.QUANTITY;
import static store.model.ProductsFormat.PROMOTION;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StockStorage {

    private static final String ADDRESS = "src/main/resources/products.md";
    private static final String SEPARATOR = ",";

    public List<Item> getAllItems() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADDRESS))) {
            skipHeaderLine(reader);
            return loadItems(reader);
        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 재고 상품(" + ADDRESS + ")을 불러오는 중 문제가 발생했습니다.");
        }
    }

    private void skipHeaderLine(BufferedReader reader) throws IOException {
        reader.readLine();
    }

    private List<Item> loadItems(BufferedReader reader) throws IOException {
        List<Item> items = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            items.add(createItem(line));
        }
        return items;
    }

    private Item createItem(String line) {
        String[] rowItem = line.split(SEPARATOR);
        String name = rowItem[NAME.getColumnPosition()];
        int price = Integer.parseInt(rowItem[PRICE.getColumnPosition()]);
        int quantity = Integer.parseInt(rowItem[QUANTITY.getColumnPosition()]);
        String promotion = rowItem[PROMOTION.getColumnPosition()];
        return new Item(name, price, quantity, promotion);
    }
}

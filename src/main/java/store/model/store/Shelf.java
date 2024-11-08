package store.model.store;

import static store.message.InputError.EXCEEDS_STOCK;
import static store.message.InputError.NON_EXISTENT_PRODUCT;

import java.util.ArrayList;
import java.util.List;
import store.dto.DisplayProduct;

public class Shelf {

    private final List<ShelfLine> lines;

    public Shelf(List<ShelfLine> lines) {
        this.lines = lines;
    }

    public void add(Product product) {
        String productName = product.getName();
        if (product.hasPromotion()) {
            ShelfLine findLine = lines.stream()
                    .filter(line -> line.getProductName().equals(productName))
                    .filter(line -> line.getPromotion().isValid())
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));
            findLine.addLast(product);
            return;
        }
        ShelfLine findLine = lines.stream()
                .filter(line -> line.getProductName().equals(productName))
                .filter(line -> !line.getPromotion().isValid())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));
        findLine.addLast(product);
    }

    public List<DisplayProduct> getInfo() {
        List<DisplayProduct> displayProducts = new ArrayList<>();
        for (ShelfLine line : lines) {
            String productName = line.getProductName();
            int price = line.getPrice();
            int quantity = getStockCount(line);
            String promotionName = "";
            if (line.getPromotion().isValid()) {
                promotionName = line.getPromotion().getName();
            }
            displayProducts.add(new DisplayProduct(productName, price, quantity, promotionName));
        }
        return displayProducts;
    }

    public List<Product> takeOut(String productName, int quantity) {
        List<ShelfLine> findLines = lines.stream()
                .filter(line -> line.getProductName().equals(productName))
                .toList();
        if (findLines.isEmpty()) {
            throw new IllegalArgumentException(NON_EXISTENT_PRODUCT.getMessage());
        }
        if (findLines.size() == 1) {
            return findLines.getFirst().takeOut(quantity);
        }

        ShelfLine promotionLine = findLines.stream()
                .filter(line -> line.getPromotion().isValid())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("있을 수 없는 일"));

        ShelfLine normalLine = findLines.stream()
                .filter(line -> !line.getPromotion().isValid())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("있을 수 없는 일"));

        if (quantity > getTotalStockCount(promotionLine, normalLine)) {
            throw new IllegalArgumentException(EXCEEDS_STOCK.getMessage());
        }
        int promotionStockCount = getStockCount(promotionLine);
        if (quantity > promotionStockCount) {
            List<Product> promotionProducts = promotionLine.takeOut(promotionStockCount);
            List<Product> normalProducts = normalLine.takeOut(quantity - promotionStockCount);
            List<Product> products = new ArrayList<>(promotionProducts);
            products.addAll(normalProducts);
            return products;
        }
        return promotionLine.takeOut(quantity);
    }

    private int getTotalStockCount(ShelfLine promotionLine, ShelfLine normalLine) {
        return getStockCount(promotionLine) + getStockCount(normalLine);
    }

    private int getStockCount(ShelfLine shelfLine) {
        return shelfLine.getProducts().size();
    }
}

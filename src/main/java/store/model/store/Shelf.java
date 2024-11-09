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
        if (product.hasPromotion()) {
            addPromotionLine(product);
            return;
        }
        addNormalLine(product);
    }

    private void addPromotionLine(Product product) {
        ShelfLine findLine = lines.stream()
                .filter(line -> line.getProductName().equals(product.getName()))
                .filter(line -> line.getPromotion().isValid())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));
        findLine.addLast(product);
    }

    private void addNormalLine(Product product) {
        ShelfLine findLine = lines.stream()
                .filter(line -> line.getProductName().equals(product.getName()))
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
            String promotionName = getPromotionName(line);
            displayProducts.add(new DisplayProduct(productName, price, quantity, promotionName));
        }
        return displayProducts;
    }

    private String getPromotionName(ShelfLine line) {
        String promotionName = "";
        if (line.getPromotion().isValid()) {
            promotionName = line.getPromotion().getName();
        }
        return promotionName;
    }

    public List<Product> takeOut(String productName, int quantity) {
        List<ShelfLine> findLines = findLinesByProductName(productName);
        validateLines(findLines);
        if (isSingleLine(findLines)) {
            return findLines.getFirst().takeOut(quantity);
        }
        ShelfLine promotionLine = findPromotionLine(findLines);
        ShelfLine normalLine = findNormalLine(findLines);
        validateStock(quantity, promotionLine, normalLine);
        return getProducts(quantity, promotionLine, normalLine);
    }

    private List<ShelfLine> findLinesByProductName(String productName) {
        return lines.stream()
                .filter(line -> line.getProductName().equals(productName))
                .toList();
    }

    private void validateLines(List<ShelfLine> findLines) {
        if (findLines.isEmpty()) {
            throw new IllegalArgumentException(NON_EXISTENT_PRODUCT.getMessage());
        }
    }

    private boolean isSingleLine(List<ShelfLine> findLines) {
        return findLines.size() == 1;
    }

    private ShelfLine findPromotionLine(List<ShelfLine> findLines) {
        return findLines.stream()
                .filter(line -> line.getPromotion().isValid())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("있을 수 없는 일"));
    }

    private ShelfLine findNormalLine(List<ShelfLine> findLines) {
        return findLines.stream()
                .filter(line -> !line.getPromotion().isValid())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("있을 수 없는 일"));
    }

    private void validateStock(int quantity, ShelfLine promotionLine, ShelfLine normalLine) {
        if (quantity > getTotalStockCount(promotionLine, normalLine)) {
            throw new IllegalArgumentException(EXCEEDS_STOCK.getMessage());
        }
    }

    private int getTotalStockCount(ShelfLine promotionLine, ShelfLine normalLine) {
        return getStockCount(promotionLine) + getStockCount(normalLine);
    }

    private int getStockCount(ShelfLine shelfLine) {
        return shelfLine.getProducts().size();
    }

    private List<Product> mergeProducts(List<Product> promotionProducts, List<Product> normalProducts) {
        List<Product> products = new ArrayList<>(promotionProducts);
        products.addAll(normalProducts);
        return products;
    }

    private List<Product> getProducts(int quantity, ShelfLine promotionLine, ShelfLine normalLine) {
        int promotionStockCount = getStockCount(promotionLine);
        if (isPromotionInsufficient(quantity, promotionStockCount)) {
            List<Product> promotionProducts = promotionLine.takeOut(promotionStockCount);
            List<Product> normalProducts = normalLine.takeOut(quantity - promotionStockCount);
            return mergeProducts(promotionProducts, normalProducts);
        }
        return promotionLine.takeOut(quantity);
    }

    private boolean isPromotionInsufficient(int quantity, int promotionStockCount) {
        return quantity > promotionStockCount;
    }
}

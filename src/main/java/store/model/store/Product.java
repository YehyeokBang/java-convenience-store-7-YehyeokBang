package store.model.store;

public class Product {

    private final String name;
    private final int price;
    private final Promotion promotion;

    public Product(String name, int price, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.promotion = promotion;
    }

    public boolean hasPromotion() {
        return promotion.isValid();
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Promotion getPromotion() {
        if (hasPromotion()) {
            return promotion;
        }
        throw new IllegalStateException("존재하지 않는 프로모션입니다.");
    }
}

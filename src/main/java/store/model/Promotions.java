package store.model;

import java.util.List;

public class Promotions {

    private final List<Promotion> promotions;

    public Promotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }
}

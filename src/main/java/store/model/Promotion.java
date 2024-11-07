package store.model;

import java.time.LocalDate;

public class Promotion {

    private final String name;
    private final int buyQuantity;
    private final int getQuantity;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String name, int buyQuantity, int getQuantity, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.getQuantity = getQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private Promotion() {
        this.name = null;
        this.buyQuantity = 0;
        this.getQuantity = 0;
        this.startDate = null;
        this.endDate = null;
    }

    public static Promotion getNoPromotion() {
        return new Promotion();
    }
}

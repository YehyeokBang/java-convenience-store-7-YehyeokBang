package store.model.event;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;

public class Promotion {

    private static final Promotion NO_PROMOTION = new Promotion(
            "null", 0, 0,
            LocalDate.MAX, LocalDate.MIN
    );

    private final String name;
    private final int buyQuantity;
    private final int freeQuantity;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotion(String name, int buyQuantity, int freeQuantity,
                     LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyQuantity = buyQuantity;
        this.freeQuantity = freeQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Promotion getNoPromotion() {
        return NO_PROMOTION;
    }

    public boolean isApplicableToday() {
        LocalDate today = getToday();
        return isStartDateValid(today) && isEndDateValid(today);
    }

    public boolean isValid() {
        return this != NO_PROMOTION;
    }

    private LocalDate getToday() {
        return DateTimes.now().toLocalDate();
    }

    private boolean isEndDateValid(LocalDate today) {
        return today.isEqual(endDate) || today.isBefore(endDate);
    }

    private boolean isStartDateValid(LocalDate today) {
        return today.isEqual(startDate) || today.isAfter(startDate);
    }

    public String getName() {
        return name;
    }

    public int getBuyQuantity() {
        return buyQuantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }
}

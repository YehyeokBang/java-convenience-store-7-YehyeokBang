package store.model.data.promotion;

import java.time.LocalDate;

public record PromotionData(
        String name,
        int buyQuantity,
        int getQuantity,
        LocalDate startDate,
        LocalDate endDate
) {

}

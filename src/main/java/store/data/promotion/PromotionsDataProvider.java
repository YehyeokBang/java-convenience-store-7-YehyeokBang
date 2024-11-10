package store.data.promotion;

import static store.data.promotion.PromotionsFormat.BUY_QUANTITY;
import static store.data.promotion.PromotionsFormat.END_DATE;
import static store.data.promotion.PromotionsFormat.GET_QUANTITY;
import static store.data.promotion.PromotionsFormat.NAME;
import static store.data.promotion.PromotionsFormat.START_DATE;
import static store.message.FileErrorMessage.EMPTY_PROMOTION_NAME;
import static store.message.FileErrorMessage.INVALID_NUMERIC;
import static store.message.FileErrorMessage.INVALID_PROMOTION_BUY_AMOUNT;
import static store.message.FileErrorMessage.INVALID_PROMOTION_DATE_FORMAT;
import static store.message.FileErrorMessage.INVALID_PROMOTION_DATE_ORDER;
import static store.message.FileErrorMessage.INVALID_PROMOTION_GET_AMOUNT;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.junit.platform.commons.util.StringUtils;
import store.data.FileReader;
import store.data.StoreDataProvider;

public class PromotionsDataProvider extends FileReader<PromotionData>
        implements StoreDataProvider<PromotionData> {

    private final String fileUrl;

    public PromotionsDataProvider(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    protected String getFileAddress() {
        return fileUrl;
    }

    @Override
    protected PromotionData parseLine(String line) {
        String[] rowPromotion = line.split(SEPARATOR);
        String name = rowPromotion[NAME.getColumnPosition()];
        int buyQuantity = parseInt(rowPromotion[BUY_QUANTITY.getColumnPosition()]);
        int getQuantity = parseInt(rowPromotion[GET_QUANTITY.getColumnPosition()]);
        LocalDate startDate = parseDate(rowPromotion[START_DATE.getColumnPosition()]);
        LocalDate endDate = parseDate(rowPromotion[END_DATE.getColumnPosition()]);
        validate(name, buyQuantity, getQuantity, startDate, endDate);
        return new PromotionData(name, buyQuantity, getQuantity, startDate, endDate);
    }

    @Override
    public List<PromotionData> getAll() {
        return readAll();
    }

    private void validate(String name, int buyQuantity, int getQuantity, LocalDate startDate, LocalDate endDate) {
        validateInputEmpty(name);
        validateBuyQuantity(buyQuantity);
        validateGetQuantity(getQuantity);
        validateDateOrder(startDate, endDate);
    }

    private void validateInputEmpty(String input) {
        if (StringUtils.isBlank(input)) {
            throw new IllegalStateException(EMPTY_PROMOTION_NAME.get());
        }
    }

    private void validateBuyQuantity(int buyQuantity) {
        if (buyQuantity < 1) {
            throw new IllegalStateException(INVALID_PROMOTION_BUY_AMOUNT.get());
        }
    }

    private void validateGetQuantity(int getQuantity) {
        if (getQuantity != 1) {
            throw new IllegalStateException(INVALID_PROMOTION_GET_AMOUNT.get());
        }
    }

    private void validateDateOrder(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalStateException(INVALID_PROMOTION_DATE_ORDER.get());
        }
    }

    private int parseInt(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalStateException(INVALID_NUMERIC.get());
        }
    }

    private LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            throw new IllegalStateException(INVALID_PROMOTION_DATE_FORMAT.get());
        }
    }
}

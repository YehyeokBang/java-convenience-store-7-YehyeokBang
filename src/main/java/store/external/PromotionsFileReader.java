package store.external;

import static store.external.PromotionsFormat.BUY_QUANTITY;
import static store.external.PromotionsFormat.END_DATE;
import static store.external.PromotionsFormat.GET_QUANTITY;
import static store.external.PromotionsFormat.NAME;
import static store.external.PromotionsFormat.START_DATE;

import java.time.LocalDate;
import store.model.Promotion;

public class PromotionsFileReader extends FileReader<Promotion> {

    private final String fileUrl;

    public PromotionsFileReader(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    protected String getFileAddress() {
        return fileUrl;
    }

    @Override
    protected Promotion parseLine(String line) {
        String[] rowPromotion = line.split(SEPARATOR);
        String name = rowPromotion[NAME.getColumnPosition()];
        int buyQuantity = Integer.parseInt(rowPromotion[BUY_QUANTITY.getColumnPosition()]);
        int getQuantity = Integer.parseInt(rowPromotion[GET_QUANTITY.getColumnPosition()]);
        LocalDate startDate = LocalDate.parse(rowPromotion[START_DATE.getColumnPosition()]);
        LocalDate endDate = LocalDate.parse(rowPromotion[END_DATE.getColumnPosition()]);
        return new Promotion(name, buyQuantity, getQuantity, startDate, endDate);
    }
}

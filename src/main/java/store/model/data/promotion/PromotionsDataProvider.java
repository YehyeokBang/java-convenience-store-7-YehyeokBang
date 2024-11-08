package store.model.data.promotion;

import static store.model.data.promotion.PromotionsFormat.BUY_QUANTITY;
import static store.model.data.promotion.PromotionsFormat.END_DATE;
import static store.model.data.promotion.PromotionsFormat.GET_QUANTITY;
import static store.model.data.promotion.PromotionsFormat.NAME;
import static store.model.data.promotion.PromotionsFormat.START_DATE;

import java.time.LocalDate;
import java.util.List;
import store.model.data.FileReader;
import store.model.data.StoreDataProvider;

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
        int buyQuantity = Integer.parseInt(rowPromotion[BUY_QUANTITY.getColumnPosition()]);
        int getQuantity = Integer.parseInt(rowPromotion[GET_QUANTITY.getColumnPosition()]);
        LocalDate startDate = LocalDate.parse(rowPromotion[START_DATE.getColumnPosition()]);
        LocalDate endDate = LocalDate.parse(rowPromotion[END_DATE.getColumnPosition()]);
        return new PromotionData(name, buyQuantity, getQuantity, startDate, endDate);
    }

    @Override
    public List<PromotionData> getAll() {
        return readAll();
    }
}

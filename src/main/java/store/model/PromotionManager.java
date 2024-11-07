package store.model;

import static store.model.PromotionFormat.NAME;
import static store.model.PromotionFormat.BUY_QUANTITY;
import static store.model.PromotionFormat.GET_QUANTITY;
import static store.model.PromotionFormat.START_DATE;
import static store.model.PromotionFormat.END_DATE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromotionManager {

    private static final String ADDRESS = "src/main/resources/promotion.md";
    private static final String SEPARATOR = ",";

    public Promotions getAllPromotions() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADDRESS))) {
            skipHeaderLine(reader);
            List<Promotion> promotions = loadPromotions(reader);
            return new Promotions(promotions);
        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 프로모션(" + ADDRESS + ")을 불러오는 중 문제가 발생했습니다.");
        }
    }

    private void skipHeaderLine(BufferedReader reader) throws IOException {
        reader.readLine();
    }

    private List<Promotion> loadPromotions(BufferedReader reader) throws IOException {
        List<Promotion> promotions = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            promotions.add(createPromotion(line));
        }
        return promotions;
    }

    private Promotion createPromotion(String line) {
        String[] rowPromotion = line.split(SEPARATOR);
        String name = rowPromotion[NAME.getColumnPosition()];
        int buyQuantity = Integer.parseInt(rowPromotion[BUY_QUANTITY.getColumnPosition()]);
        int getQuantity = Integer.parseInt(rowPromotion[GET_QUANTITY.getColumnPosition()]);
        LocalDate startDate = LocalDate.parse(rowPromotion[START_DATE.getColumnPosition()]);
        LocalDate endDate = LocalDate.parse(rowPromotion[END_DATE.getColumnPosition()]);
        return new Promotion(name, buyQuantity, getQuantity, startDate, endDate);
    }
}

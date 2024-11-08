package store.model.data.promotion;

public enum PromotionsFormat {

    NAME(0),
    BUY_QUANTITY(1),
    GET_QUANTITY(2),
    START_DATE(3),
    END_DATE(4),
    ;

    private final int columnPosition;

    PromotionsFormat(int columnPosition) {
        this.columnPosition = columnPosition;
    }

    public int getColumnPosition() {
        return columnPosition;
    }
}

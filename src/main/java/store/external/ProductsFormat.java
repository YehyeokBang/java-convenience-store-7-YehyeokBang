package store.external;

public enum ProductsFormat {

    NAME(0),
    PRICE(1),
    QUANTITY(2),
    PROMOTION(3),
    ;

    private final int columnPosition;

    ProductsFormat(int columnPosition) {
        this.columnPosition = columnPosition;
    }

    public int getColumnPosition() {
        return columnPosition;
    }
}

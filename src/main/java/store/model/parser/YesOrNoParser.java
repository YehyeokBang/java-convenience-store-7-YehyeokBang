package store.model.parser;

import static store.message.InputErrorMessage.*;

public class YesOrNoParser implements Parser<String, Boolean> {

    @Override
    public Boolean parse(String rawInput) {
        if (isYes(rawInput)) {
            return true;
        }
        if (isNo(rawInput)) {
            return false;
        }
        throw new IllegalArgumentException(INVALID_FORMAT.get());
    }

    private static boolean isYes(String rawInput) {
        return rawInput.equals("Y") || rawInput.equals("y");
    }

    private boolean isNo(String rawInput) {
        return rawInput.equals("N") || rawInput.equals("n");
    }
}

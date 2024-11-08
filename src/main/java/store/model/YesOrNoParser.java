package store.model;

import static store.message.InputError.*;

public class YesOrNoParser {

    public boolean parse(String rawInput) {
        if (isYes(rawInput)) {
            return true;
        }
        if (isNo(rawInput)) {
            return false;
        }
        throw new IllegalStateException(INVALID_FORMAT.getMessage());
    }

    private static boolean isYes(String rawInput) {
        return rawInput.equals("Y") || rawInput.equals("y");
    }

    private boolean isNo(String rawInput) {
        return rawInput.equals("N") || rawInput.equals("n");
    }
}

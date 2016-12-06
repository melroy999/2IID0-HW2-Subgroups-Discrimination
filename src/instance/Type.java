package instance;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Type {
    NUMERIC, BOOLEAN, SET, RANGE;

    private static final Pattern RANGE_PATTERN = Pattern.compile("(\\[[0-9]+-[0-9]+\\])");

    public static Type getType(String type) {
        switch (type) {
            case "numeric":
                return NUMERIC;
            case "{0,1}":
                return BOOLEAN;
            default:
                return isRangeType(type) ? RANGE : SET;
        }
    }

    public static boolean isRangeType(String line) {
        Matcher matcher = RANGE_PATTERN.matcher(line);
        return matcher.find();
    }
}

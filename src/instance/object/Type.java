package instance.object;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enum holding different kind of value types we want to support.
 */
public enum Type {
    NUMERIC, BOOLEAN, SET, RANGE;

    private static final Pattern RANGE_PATTERN = Pattern.compile("(\\[[0-9]+-[0-9]+\\])");

    /**
     * Get a type based on the string representation.
     *
     * @param type String representation of the type.
     * @return String representation of the type.
     */
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

    /**
     * Whether the given string is of the range type.
     *
     * @param line The value to evaluate.
     * @return True if of the range type, false otherwise.
     */
    public static boolean isRangeType(String line) {
        Matcher matcher = RANGE_PATTERN.matcher(line);
        return matcher.find();
    }
}

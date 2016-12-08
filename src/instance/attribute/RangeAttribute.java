package instance.attribute;

import instance.object.Type;

import java.util.Arrays;

/**
 * Representation of a range attribute.
 */
public class RangeAttribute extends AbstractAttribute {
    private final Range[] values;

    /**
     * Create an attribute.
     *
     * @param name The name of the attribute.
     * @param id The id of the attribute.
     * @param valueSet The list of values.
     */
    public RangeAttribute(String name, int id, String valueSet) {
        super(name, id);

        String[] components = valueSet.split(",");

        values = new Range[components.length];

        for(int i = 0; i < values.length; i++) {
            String component = components[i];
            String[] range = component.replaceAll("(\\[|\\])","").split("-");
            values[i] = new Range(Integer.valueOf(range[0]), Integer.valueOf(range[1]));
        }
    }

    /**
     * Get the type of the attribute.
     *
     * @return The type of the attribute.
     */
    @Override
    public Type getType() {
        return Type.RANGE;
    }

    /**
     * Get the array of values this attribute can take.
     *
     * @return The array of values this attribute can take.
     */
    public Range[] getValues() {
        return values;
    }

    /**
     * Class representing a range of values.
     */
    private class Range {
        private final int min;
        private final int max;

        /**
         * Create a range with the given min and max bounds.
         *
         * @param min The minimum.
         * @param max The maximum.
         */
        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        /**
         * Get the minimum of the range.
         *
         * @return The minimum of the range.
         */
        public int getMin() {
            return min;
        }

        /**
         * Get the maximum of the range.
         *
         * @return The maximum of the range.
         */
        public int getMax() {
            return max;
        }

        @Override
        public String toString() {
            return "[" + min + "-" + max + "]";
        }
    }

    /**
     * Converts the attribute to its string representation.
     *
     * @return The name of the attribute, with @attribute as prefix.
     */
    @Override
    public String toString() {
        return super.toString() + " " + Arrays.toString(values);
    }
}

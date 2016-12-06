package instance.attribute;

import instance.Type;
import instance.search.SubGroup;

import java.util.Arrays;

public class RangeAttribute extends AbstractAttribute {
    private final Range[] values;

    protected RangeAttribute(String name, int id, String valueSet) {
        super(name, id);

        String[] components = valueSet.split(",");

        values = new Range[components.length];

        for(int i = 0; i < values.length; i++) {
            String component = components[i];
            String[] range = component.replaceAll("(\\[|\\])","").split("-");
            values[i] = new Range(Integer.valueOf(range[0]), Integer.valueOf(range[1]));
        }
    }

    @Override
    public Type getType() {
        return Type.RANGE;
    }

    @Override
    public boolean isPartOfSubgroup(SubGroup subGroup, String value) {
        return value.equals(subGroup.getValue());
    }

    public Range[] getValues() {
        return values;
    }

    public class Range {
        private final int min;
        private final int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        @Override
        public String toString() {
            return "[" + min + "-" + max + "]";
        }
    }

    @Override
    public String toString() {
        return super.toString() + " " + Arrays.toString(values);
    }
}

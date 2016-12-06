package instance;

import instance.attribute.AbstractAttribute;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Instance {
    private final String[] values;

    public Instance(String line) {
        this.values = line.split(",");
    }

    @Override
    public String toString() {
        return Arrays.toString(values).replaceAll("\\{|\\}","");
    }

    public String getValue(AbstractAttribute attribute) {
        return values[attribute.getId()];
    }

    public String getTarget() {
        return values[values.length - 2];
    }
}

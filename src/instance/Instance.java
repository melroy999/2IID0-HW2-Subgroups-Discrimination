package instance;

import instance.attribute.AbstractAttribute;

import java.util.Arrays;

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
}

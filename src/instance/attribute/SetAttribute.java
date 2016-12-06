package instance.attribute;

import instance.Type;

import java.util.Arrays;

public class SetAttribute extends AbstractAttribute {
    private final String[] values;

    protected SetAttribute(String name, int id, String valueSet) {
        super(name, id);
        this.values = valueSet.replaceAll(("(\\{|\\})"),"").split(",");
    }

    public String[] getValues() {
        return values;
    }

    @Override
    public Type getType() {
        return Type.SET;
    }

    @Override
    public String toString() {
        return super.toString() + " " + Arrays.toString(values);
    }
}

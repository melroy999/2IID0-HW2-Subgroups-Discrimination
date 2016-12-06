package instance.attribute;

import instance.Type;

public class NumericAttribute extends AbstractAttribute {
    protected NumericAttribute(String name, int id) {
        super(name, id);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + " numeric";
    }
}

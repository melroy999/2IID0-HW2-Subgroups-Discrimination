package instance.attribute;

import instance.Type;

public class BooleanAttribute extends AbstractAttribute {
    protected BooleanAttribute(String name, int id) {
        super(name, id);
    }

    @Override
    public Type getType() {
        return Type.BOOLEAN;
    }

    @Override
    public String toString() {
        return super.toString() + " {0,1}";
    }
}

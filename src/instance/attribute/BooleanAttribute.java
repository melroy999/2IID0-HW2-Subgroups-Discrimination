package instance.attribute;

import instance.Type;
import instance.search.SubGroup;

public class BooleanAttribute extends AbstractAttribute {
    protected BooleanAttribute(String name, int id) {
        super(name, id);
    }

    @Override
    public Type getType() {
        return Type.BOOLEAN;
    }

    @Override
    public boolean isPartOfSubgroup(SubGroup subGroup, String value) {
        return value.equals(subGroup.getValue());
    }

    @Override
    public String toString() {
        return super.toString() + " {0,1}";
    }
}

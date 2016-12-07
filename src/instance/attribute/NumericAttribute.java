package instance.attribute;

import instance.Type;
import instance.search.SubGroup;

public class NumericAttribute extends AbstractAttribute {
    protected NumericAttribute(String name, int id) {
        super(name, id);
    }

    @Override
    public Type getType() {
        return Type.NUMERIC;
    }

    @Override
    public boolean isPartOfSubgroup(SubGroup subGroup, String value) {
        return Double.valueOf(value) <= Double.valueOf(subGroup.getValue());
    }

    @Override
    public String toString() {
        return super.toString() + " numeric";
    }
}

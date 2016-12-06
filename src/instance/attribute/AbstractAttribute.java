package instance.attribute;

import instance.Type;

import java.util.Arrays;

public abstract class AbstractAttribute {
    private final String name;
    private final int id;

    protected AbstractAttribute(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public abstract Type getType();

    public static AbstractAttribute getAttribute(String line, int id) {
        //Break the line on whitespace.
        String[] components = line.split("\\s");

        //Set the name, which is the second value in the data set.
        String name = components[1];

        //Determine the Type.
        Type type = Type.getType(components[2]);

        //Based on the type, create an actual instance.
        switch (type) {
            case BOOLEAN:
                return new BooleanAttribute(name, id);
            case NUMERIC:
                return new NumericAttribute(name, id);
            case SET:
                return new SetAttribute(name, id, components[2]);
            case RANGE:
                return new RangeAttribute(name, id, components[2]);
        }
        return null;
    }

    @Override
    public String toString() {
        return "@attribute " + getName();
    }
}

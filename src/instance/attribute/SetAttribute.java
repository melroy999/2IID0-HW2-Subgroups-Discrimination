package instance.attribute;

import instance.object.Type;

import java.util.Arrays;

/**
 * Representation of a set attribute.
 */
public class SetAttribute extends AbstractAttribute {
    private final String[] values;

    /**
     * Create an attribute.
     *
     * @param name The name of the attribute.
     * @param id The id of the attribute.
     * @param valueSet The list of values.
     */
    public SetAttribute(String name, int id, String valueSet) {
        super(name, id);
        this.values = valueSet.split(",");
    }

    /**
     * Get the array of values this attribute can take.
     *
     * @return The array of values this attribute can take.
     */
    public String[] getValues() {
        return values;
    }

    /**
     * Get the type of the attribute.
     *
     * @return The type of the attribute.
     */
    @Override
    public Type getType() {
        return Type.SET;
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

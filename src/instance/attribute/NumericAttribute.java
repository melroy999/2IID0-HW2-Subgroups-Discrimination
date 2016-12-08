package instance.attribute;

import instance.object.Type;

/**
 * Representation of the numeric attribute.
 */
public class NumericAttribute extends AbstractAttribute {
    /**
     * Create an attribute.
     *
     * @param name The name of the attribute.
     * @param id The id of the attribute.
     */
    public NumericAttribute(String name, int id) {
        super(name, id);
    }

    /**
     * Get the type of the attribute.
     *
     * @return The type of the attribute.
     */
    @Override
    public Type getType() {
        return Type.NUMERIC;
    }

    /**
     * Converts the attribute to its string representation.
     *
     * @return The name of the attribute, with @attribute as prefix.
     */
    @Override
    public String toString() {
        return super.toString() + " numeric";
    }
}

package instance.attribute;

import instance.object.Type;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract representation of an attribute.
 */
public abstract class AbstractAttribute {
    //Pattern used for attribute detection.
    private static final Pattern ATTRIBUTE_MATCH_PATTERN = Pattern.compile("@attribute\\s+([']?(?:[^']*)[']?)\\s+[{]?(.*?)[}]?");

    //Values representing an attribute.
    private final String name;
    private final int id;

    /**
     * Create an attribute.
     *
     * @param name The name of the attribute.
     * @param id The id of the attribute.
     */
    public AbstractAttribute(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Get the name of the attribute.
     *
     * @return The name of the attribute.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the id of the attribute.
     *
     * @return The id of the attribute.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the type of the attribute.
     *
     * @return The type of the attribute.
     */
    public abstract Type getType();

    /**
     * Extract the attribute from the line, and create it.
     *
     * @param line The line to parse.
     * @param id The id to give the attribute.
     * @return An attribute object corresponding to the given line.
     * @throws Exception Throws an exception if no attributes can be found in the given line.
     */
    public static AbstractAttribute getAttribute(String line, int id) throws Exception {
        //Get the pattern matches.
        Matcher matches = ATTRIBUTE_MATCH_PATTERN.matcher(line);

        //Find matches.
        if(!matches.matches()) {
            throw new Exception("No attribute found.");
        }

        //Set the name, which is the first group in the regex.
        String name = matches.group(1);

        //The values are the second group in the regex.
        String value = matches.group(2);

        //Determine the Type.
        Type type = Type.getType(value);

        //Based on the type, create an actual instance.
        switch (type) {
            case BOOLEAN:
                return new BooleanAttribute(name, id);
            case NUMERIC:
                return new NumericAttribute(name, id);
            case SET:
                return new SetAttribute(name, id, value);
            case RANGE:
                return new RangeAttribute(name, id, value);
        }
        return null;
    }

    /**
     * Converts the attribute to its string representation.
     *
     * @return The name of the attribute, with @attribute as prefix.
     */
    @Override
    public String toString() {
        return "@attribute " + getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractAttribute)) return false;

        AbstractAttribute that = (AbstractAttribute) o;

        if (id != that.id) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + id;
        return result;
    }
}

package instance.attribute;

import instance.Type;
import instance.search.SubGroup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractAttribute {
    private static final Pattern ATTRIBUTE_MATCH_PATTERN = Pattern.compile("@attribute\\s+([']?(?:[^']*)[']?)\\s+[{]?(.*?)[}]?");
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

    public abstract boolean isPartOfSubgroup(SubGroup subGroup, String value);

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

    @Override
    public String toString() {
        return "@attribute " + getName();
    }
}

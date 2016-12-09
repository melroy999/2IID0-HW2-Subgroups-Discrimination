package instance.object;

import instance.attribute.AbstractAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Instance {
    //The list of values of the instance.
    private final String[] values;
    private final int target;

    /**
     * Create an instance.
     *
     * @param line The line representing the instance.
     * @param target The id of the target attribute.
     */
    public Instance(String line, int target) {
        //Parse the string manually instead of splitting it, as the data containsInstance some annoying inputs that split cannot handle.
        List<String> components = parseString(line);

        //Convert to array and set.
        this.values = components.toArray(new String[components.size()]);

        //Set the target index.
        this.target = target;
    }

    /**
     * Parse the string character by character.
     *
     * @param line The string to parse.
     * @return A list of the values for all the attributes.
     */
    private List<String> parseString(String line) {
        List<String> components = new ArrayList<>();
        boolean skipCommas = false;
        String component = "";
        for(char c : line.toCharArray()) {
            if(skipCommas) {
                component += c;
                if(c == '\'') {
                    skipCommas = false;
                }
            } else {
                if(c == ',') {
                    components.add(component);
                    component = "";
                    continue;
                }

                component += c;
                if(c == '\'') {
                    skipCommas = true;
                }
            }
        }

        //Add the remaining component.
        components.add(component);
        return components;
    }

    /**
     * Get the value for the given attribute.
     *
     * @param attribute The attribute we want the value of.
     * @return The value in the string array at the index of the attribute.
     */
    public String getValue(AbstractAttribute attribute) {
        return values[attribute.getId()];
    }

    /**
     * Get the value of the target attribute.
     *
     * @return The value in the array at the target index.
     */
    public String getTargetValue() {
        return values[target];
    }

    /**
     * Convert the list to a string, without the parenthesis.
     *
     * @return String representation of the array, with the parenthesis removed.
     */
    @Override
    public String toString() {
        return Arrays.toString(values).replaceAll("\\{|\\}","");
    }
}

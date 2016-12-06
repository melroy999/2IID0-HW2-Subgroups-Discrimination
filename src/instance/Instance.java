package instance;

import instance.attribute.AbstractAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Instance {
    private final String[] values;

    public Instance(String line) {
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

        //Convert to array and set.
        this.values = components.toArray(new String[components.size()]);
    }

    @Override
    public String toString() {
        return Arrays.toString(values).replaceAll("\\{|\\}","");
    }

    public String getValue(AbstractAttribute attribute) {
        return values[attribute.getId()];
    }

    public String getTarget() {
        return values[values.length - 2];
    }
}

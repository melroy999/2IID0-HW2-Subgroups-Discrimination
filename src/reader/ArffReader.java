package reader;

import instance.ArffFile;
import instance.Instance;
import instance.attribute.AbstractAttribute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArffReader {
    private static final List<String> lines = new ArrayList<>();

    public static ArffFile getArffFile(String filePath) throws Exception {
        lines.clear();
        lines.addAll(FileLoader.readAllLines(filePath));

        List<AbstractAttribute> attributes = new ArrayList<>();
        String relation = "";
        List<Instance> instances = new ArrayList<>();

        int attributeCounter = 0;
        for(String line : lines) {
            if(line.startsWith("@attribute")) {
                attributes.add(AbstractAttribute.getAttribute(line, attributeCounter++));
            } else if(line.startsWith("@relation")) {
                relation = line.replaceFirst("@relation ","").replaceAll("'","");
            } else if(line.contains(",")) {
                instances.add(new Instance(line));
            }
        }

        return new ArffFile(attributes, instances, relation);
    }
}

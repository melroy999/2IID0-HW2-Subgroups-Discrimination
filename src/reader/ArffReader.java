package reader;

import instance.object.ArffFile;
import instance.object.Instance;
import instance.attribute.AbstractAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper for ARFF file loading.
 */
public class ArffReader {
    //The lines of the file.
    private static final List<String> lines = new ArrayList<>();

    /**
     * Read the given arff file, and convert it to an object.
     *
     * @param filePath The path to the file we want to load.
     * @return The arff file as an object.
     * @throws Exception Throws an exception if the file cannot be loaded.
     */
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
                instances.add(new Instance(line, attributes.size() - 1));
            }
        }

        return new ArffFile(attributes, instances, relation, attributes.size() - 1);
    }
}

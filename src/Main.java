import instance.ArffFile;
import instance.attribute.AbstractAttribute;
import instance.search.BeamSearch;
import reader.ArffReader;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            ArffFile file = ArffReader.getArffFile("/dataset.arff");

            for(AbstractAttribute attribute : file.getAttributes()) {
                System.out.println(attribute);
            }

            //System.out.println(BeamSearch.search(file, 1, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

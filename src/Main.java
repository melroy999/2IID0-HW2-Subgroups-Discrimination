import instance.ArffFile;
import instance.attribute.AbstractAttribute;
import instance.evaluate.SimpleEvaluator;
import instance.search.BeamSearch;
import reader.ArffReader;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        try {
            ArffFile file = ArffReader.getArffFile("/dataset.arff");

            HashSet<String> blacklist = new HashSet<>();
            blacklist.add("decision_o");
            System.out.println(BeamSearch.search(file, new SimpleEvaluator(), 4, 1, blacklist));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

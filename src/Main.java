import instance.ArffFile;
import instance.heuristic.WeightedRelativeAccuracyHeuristic;
import instance.search.BeamSearch;
import reader.ArffReader;

import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        try {
            ArffFile file = ArffReader.getArffFile("/dataset.arff");

            HashSet<String> blacklist = new HashSet<>();
            blacklist.add("decision_o");
            System.out.println(BeamSearch.search(file, new WeightedRelativeAccuracyHeuristic(), 4, 1, blacklist));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

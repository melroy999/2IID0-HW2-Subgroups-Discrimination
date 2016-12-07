import instance.ArffFile;
import instance.heuristic.WeightedRelativeAccuracyHeuristic;
import instance.search.BeamSearch;
import instance.search.SubGroup;
import reader.ArffReader;

import java.util.Arrays;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        try {
            ArffFile file = ArffReader.getArffFile("/dataset.arff");

            HashSet<String> blacklist = new HashSet<>();
            blacklist.add("decision_o");

            for(SubGroup subGroup : BeamSearch.search(file, new WeightedRelativeAccuracyHeuristic(), 5, 5, blacklist)) {
                System.out.println(subGroup);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

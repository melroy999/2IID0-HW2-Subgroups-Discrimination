import instance.ArffFile;
import instance.heuristic.SensitivityQualityMeasureHeuristic;
import instance.heuristic.SpecificityQualityMeasureHeuristic;
import instance.heuristic.WeightedRelativeAccuracyHeuristic;
import instance.heuristic.X2Heuristic;
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

            SubGroup[][] wraResult = BeamSearch.search(file, new WeightedRelativeAccuracyHeuristic(), 5, 5, blacklist);
            for(SubGroup subGroup : wraResult[wraResult.length - 1]) {
                System.out.println(subGroup);
            }

            SubGroup[][] sensitivityResult = BeamSearch.search(file, new SensitivityQualityMeasureHeuristic(), 5, 5, blacklist);
            for(SubGroup subGroup : sensitivityResult[sensitivityResult.length - 1]) {
                System.out.println(subGroup);
            }

            SubGroup[][] specificityResult = BeamSearch.search(file, new SpecificityQualityMeasureHeuristic(), 5, 5, blacklist);
            for(SubGroup subGroup : specificityResult[specificityResult.length - 1]) {
                System.out.println(subGroup);
            }

            SubGroup[][] x2Result = BeamSearch.search(file, new X2Heuristic(), 5, 5, blacklist);
            for(SubGroup subGroup : x2Result[x2Result.length - 1]) {
                System.out.println(subGroup);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

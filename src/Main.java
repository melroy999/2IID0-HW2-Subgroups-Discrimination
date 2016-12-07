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

            System.out.println("Weighted relative accuracy: ");
            System.out.println("((p + n) / (P + N)) * (p / (p + n) - P / (P + N))");
            SubGroup[][] wraResult = BeamSearch.search(file, new WeightedRelativeAccuracyHeuristic(), 2, 5, blacklist);
            for(SubGroup subGroup : wraResult[wraResult.length - 1]) {
                System.out.println("(eval: " + subGroup.getEvaluation() + "): \t\t" + subGroup.shortNotation());
            }
            System.out.println();

            System.out.println("Sensitivity quality measure: ");
            System.out.println("p / P");
            SubGroup[][] sensitivityResult = BeamSearch.search(file, new SensitivityQualityMeasureHeuristic(), 2, 5, blacklist);
            for(SubGroup subGroup : sensitivityResult[sensitivityResult.length - 1]) {
                System.out.println("(eval: " + subGroup.getEvaluation() + "): \t\t" + subGroup.shortNotation());
            }
            System.out.println();

            System.out.println("Specificity quality measure: ");
            System.out.println("1 - n / N");
            SubGroup[][] specificityResult = BeamSearch.search(file, new SpecificityQualityMeasureHeuristic(), 2, 5, blacklist);
            for(SubGroup subGroup : specificityResult[specificityResult.length - 1]) {
                System.out.println("(eval: " + subGroup.getEvaluation() + "): \t\t" + subGroup.shortNotation());
            }
            System.out.println();

            System.out.println("x2:");
            System.out.println("((p * N - P * n) * (p * N - P * n) / (P + N)) * ((P + N) * (P + N) / (P * N * (p + n) * (P + N - p - n)))");
            SubGroup[][] x2Result = BeamSearch.search(file, new X2Heuristic(), 5, 5, blacklist);
            for(SubGroup subGroup : x2Result[x2Result.length - 1]) {
                System.out.println("(eval: " + subGroup.getEvaluation() + "): \t\t" + subGroup.shortNotation());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

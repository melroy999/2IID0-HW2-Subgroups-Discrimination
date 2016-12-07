import instance.ArffFile;
import instance.Instance;
import instance.attribute.AbstractAttribute;
import instance.heuristic.SensitivityQualityMeasureHeuristic;
import instance.heuristic.SpecificityQualityMeasureHeuristic;
import instance.heuristic.WeightedRelativeAccuracyHeuristic;
import instance.heuristic.X2Heuristic;
import instance.search.BeamSearch;
import instance.search.SubGroup;
import reader.ArffReader;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;

public class Main {
    private static final int DECIMAL_PLACES = 15;

    public static void main(String[] args) {
        try {
            ArffFile file = ArffReader.getArffFile("/dataset.arff");

            HashSet<String> blacklist = new HashSet<>();
            blacklist.add("decision_o");

            System.out.println("= Weighted relative accuracy ===============================================================================");
            System.out.println("((p + n) / (P + N)) * (p / (p + n) - P / (P + N))");
            SubGroup[][] wraResult = BeamSearch.search(file, new WeightedRelativeAccuracyHeuristic(), 3, 10, blacklist);
            printFullResultArray(wraResult);

            System.out.println("= Sensitivity quality measure ==============================================================================");
            System.out.println("p / P");
            SubGroup[][] sensitivityResult = BeamSearch.search(file, new SensitivityQualityMeasureHeuristic(), 3, 10, blacklist);
            printFullResultArray(sensitivityResult);

            System.out.println("= Specificity quality measure ==============================================================================");
            System.out.println("1 - n / N");
            SubGroup[][] specificityResult = BeamSearch.search(file, new SpecificityQualityMeasureHeuristic(), 3, 10, blacklist);
            printFullResultArray(specificityResult);

            System.out.println("= x2 =======================================================================================================");
            System.out.println("(((p * N - P * n) * (p * N - P * n)) / (P + N)) * ((P + N) * (P + N) / (P * N * (p + n) * (P + N - p - n)))");
            SubGroup[][] x2Result = BeamSearch.search(file, new X2Heuristic(), 3, 10, blacklist);
            printFullResultArray(x2Result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printFullResultArray(SubGroup[][] wraResult) {
        for(int i = 0; i < wraResult.length; i++) {
            System.out.println();
            SubGroup[] subGroups = wraResult[i];
            System.out.println("Level-" + (i + 1) + ":");
            for(SubGroup subGroup : subGroups) {
                System.out.println("\t (eval: " + String.format("%." + DECIMAL_PLACES + "f", subGroup.getEvaluation()) + "): \t" + subGroup.shortNotation());
            }
        }
        System.out.println();
    }
}

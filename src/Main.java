import instance.attribute.AbstractAttribute;
import instance.heuristic.SensitivityQualityMeasureHeuristic;
import instance.heuristic.SpecificityQualityMeasureHeuristic;
import instance.heuristic.WeightedRelativeAccuracyHeuristic;
import instance.heuristic.X2Heuristic;
import instance.object.ArffFile;
import instance.object.Group;
import instance.object.Instance;
import instance.search.BeamSearch;
import reader.ArffReader;

import java.util.HashSet;

public class Main {
    private static final int DECIMAL_PLACES = 6;
    private static int SEARCH_DEPTH = 2;
    private static int SEARCH_WIDTH = 25;
    private static int MINIMUM_GROUP_SIZE = 1000;
    private static double MAXIMUM_FRACTION = 0.9;

    //Changing this will cause a mismatch between this program and cortana. Be warned.
    private static final boolean checkValue = false;

    public static void main(String[] args) {
        if(args.length < 4) {
            System.out.println("Taking default values SEARCH_DEPTH = " + SEARCH_DEPTH + ", SEARCH_WIDTH = " + SEARCH_WIDTH + ", MINIMUM_GROUP_SIZE = " + MINIMUM_GROUP_SIZE + ", MAXIMUM_FRACTION = " + MAXIMUM_FRACTION + ".");
        } else {
            SEARCH_DEPTH = Integer.valueOf(args[0]);
            SEARCH_WIDTH = Integer.valueOf(args[1]);
            MINIMUM_GROUP_SIZE = Integer.valueOf(args[2]);
            MAXIMUM_FRACTION = Integer.valueOf(args[3]);
            System.out.println("Taking values SEARCH_DEPTH = " + SEARCH_DEPTH + ", SEARCH_WIDTH = " + SEARCH_WIDTH + ", MINIMUM_GROUP_SIZE = " + MINIMUM_GROUP_SIZE + ", MAXIMUM_FRACTION = " + MAXIMUM_FRACTION + ".");
        }

        try {
            ArffFile file = ArffReader.getArffFile("/dataset.arff");

            HashSet<String> blacklist = new HashSet<>();
            blacklist.add("decision_o");
            blacklist.add("decision");

            System.out.println("= Weighted relative accuracy ===============================================================================");
            System.out.println("Heuristic: ((p + n) / (P + N)) * (p / (p + n) - P / (P + N))");
            Group[][] wraResult = BeamSearch.search(file, new WeightedRelativeAccuracyHeuristic(), SEARCH_WIDTH, SEARCH_DEPTH, MINIMUM_GROUP_SIZE, MAXIMUM_FRACTION, checkValue, blacklist);
            printFullResultArray(wraResult);
            //printEqualityCheck(wraResult);

            System.out.println("= Sensitivity quality measure ==============================================================================");
            System.out.println("Heuristic: p / P");
            Group[][] sensitivityResult = BeamSearch.search(file, new SensitivityQualityMeasureHeuristic(), SEARCH_WIDTH, SEARCH_DEPTH, MINIMUM_GROUP_SIZE, MAXIMUM_FRACTION, checkValue, blacklist);
            printFullResultArray(sensitivityResult);
            //printEqualityCheck(sensitivityResult);

            System.out.println("= Specificity quality measure ==============================================================================");
            System.out.println("Heuristic: 1 - n / N");
            Group[][] specificityResult = BeamSearch.search(file, new SpecificityQualityMeasureHeuristic(), SEARCH_WIDTH, SEARCH_DEPTH, MINIMUM_GROUP_SIZE, MAXIMUM_FRACTION, checkValue, blacklist);
            printFullResultArray(specificityResult);
            //printEqualityCheck(specificityResult);

            System.out.println("= x2 =======================================================================================================");
            System.out.println("Heuristic: (((p * N - P * n) * (p * N - P * n)) / (P + N)) * ((P + N) * (P + N) / (P * N * (p + n) * (P + N - p - n)))");
            Group[][] x2Result = BeamSearch.search(file, new X2Heuristic(), SEARCH_WIDTH, SEARCH_DEPTH, MINIMUM_GROUP_SIZE, MAXIMUM_FRACTION, checkValue, blacklist);
            printFullResultArray(x2Result);
            //printEqualityCheck(x2Result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printFullResultArray(Group[][] wraResult) {
        for(int i = 0; i < wraResult.length; i++) {
            System.out.println();
            Group[] subGroups = wraResult[i];
            System.out.println("Level-" + (i + 1) + ":");
            for(Group subGroup : subGroups) {
                System.out.println("\t (eval: " + String.format("%." + DECIMAL_PLACES + "f", subGroup.getResult().getEvaluationValue()) + ") \t" + subGroup);
                System.out.println("\t\t" + subGroup.getResult());
            }
        }
        System.out.println();
    }

    private static void printEqualityCheck(Group[][] groupCollection) {
        System.out.println("Equality check: ");
        boolean isFirstFirst = true;
        for(int i = 0; i < groupCollection.length; i++) {
            boolean isFirst = true;
            Group[] groups = groupCollection[i];
            for(Group subGroup : groups) {
                for(Group subGroup2 : groups) {
                    if(subGroup != subGroup2 && subGroup.isDuplicateOf(subGroup2, checkValue, false)) {
                        if(isFirst) {
                            System.out.println("Level-" + (i + 1) + ":");
                            isFirst = false;
                            isFirstFirst = false;
                        }
                        System.out.println("Equality for ");
                        System.out.println("\t (" + String.format("%." + DECIMAL_PLACES + "f", subGroup.getResult().getEvaluationValue()) + ") \t" + subGroup);
                        System.out.println("\t (" + String.format("%." + DECIMAL_PLACES + "f", subGroup2.getResult().getEvaluationValue()) + ") \t" + subGroup2);
                        System.out.println();
                    }
                }
            }
        }
        if(isFirstFirst) {
            System.out.println("No equalities detected.");
            System.out.println();
        }
    }
}

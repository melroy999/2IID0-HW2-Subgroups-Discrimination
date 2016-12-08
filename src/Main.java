import instance.object.ArffFile;
import reader.ArffReader;

import java.util.HashSet;

public class Main {
    private static final int DECIMAL_PLACES = 15;
    private static int SEARCH_DEPTH = 3;
    private static int SEARCH_WIDTH = 5;
    private static final boolean allowSubgroupWithDifferentValues = false;

    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Taking default values SEARCH_DEPTH = " + SEARCH_DEPTH + ", SEARCH_WIDTH = " + SEARCH_WIDTH + ".");
        } else {
            SEARCH_DEPTH = Integer.valueOf(args[0]);
            SEARCH_WIDTH = Integer.valueOf(args[1]);
            System.out.println("Taking values SEARCH_DEPTH = " + SEARCH_DEPTH + ", SEARCH_WIDTH = " + SEARCH_WIDTH + ".");
        }

        try {
            ArffFile file = ArffReader.getArffFile("/dataset.arff");

            HashSet<String> blacklist = new HashSet<>();
            blacklist.add("decision_o");
            blacklist.add("decision");

            /*System.out.println("= Weighted relative accuracy ===============================================================================");
            System.out.println("Heuristic: ((p + n) / (P + N)) * (p / (p + n) - P / (P + N))");
            SubGroup[][] wraResult = BeamSearch.search(file, new WeightedRelativeAccuracyHeuristic(), SEARCH_DEPTH, SEARCH_WIDTH, blacklist, allowSubgroupWithDifferentValues);
            printFullResultArray(wraResult);
            printEqualityCheck(wraResult);

            System.out.println("= Sensitivity quality measure ==============================================================================");
            System.out.println("Heuristic: p / P");
            SubGroup[][] sensitivityResult = BeamSearch.search(file, new SensitivityQualityMeasureHeuristic(), SEARCH_DEPTH, SEARCH_WIDTH, blacklist, allowSubgroupWithDifferentValues);
            printFullResultArray(sensitivityResult);
            printEqualityCheck(sensitivityResult);

            System.out.println("= Specificity quality measure ==============================================================================");
            System.out.println("Heuristic: 1 - n / N");
            SubGroup[][] specificityResult = BeamSearch.search(file, new SpecificityQualityMeasureHeuristic(), SEARCH_DEPTH, SEARCH_WIDTH, blacklist, allowSubgroupWithDifferentValues);
            printFullResultArray(specificityResult);
            printEqualityCheck(specificityResult);

            System.out.println("= x2 =======================================================================================================");
            System.out.println("Heuristic: (((p * N - P * n) * (p * N - P * n)) / (P + N)) * ((P + N) * (P + N) / (P * N * (p + n) * (P + N - p - n)))");
            SubGroup[][] x2Result = BeamSearch.search(file, new X2Heuristic(), SEARCH_DEPTH, SEARCH_WIDTH, blacklist, allowSubgroupWithDifferentValues);
            printFullResultArray(x2Result);
            printEqualityCheck(x2Result);*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private static void printFullResultArray(SubGroup[][] wraResult) {
        for(int i = 0; i < wraResult.length; i++) {
            System.out.println();
            SubGroup[] subGroups = wraResult[i];
            System.out.println("Level-" + (i + 1) + ":");
            for(SubGroup subGroup : subGroups) {
                System.out.println("\t (eval: " + String.format("%." + DECIMAL_PLACES + "f", subGroup.getEvaluation()) + ") \t" + subGroup.shortNotation());
                System.out.println("\t\t" + subGroup.getHeuristic());
            }
        }
        System.out.println();
    }*/

    /*private static void printEqualityCheck(SubGroup[][] wraResult) {
        System.out.println("Equality check: ");
        boolean isFirstFirst = true;
        for(int i = 0; i < wraResult.length; i++) {
            boolean isFirst = true;
            SubGroup[] subGroups = wraResult[i];
            for(SubGroup subGroup : subGroups) {
                for(SubGroup subGroup2 : subGroups) {
                    if(subGroup != subGroup2 && subGroup.recursiveHasAllSubgroups(subGroup2, allowSubgroupWithDifferentValues)) {
                        if(isFirst) {
                            System.out.println("Level-" + (i + 1) + ":");
                            isFirst = false;
                            isFirstFirst = false;
                        }
                        System.out.println("Equality for ");
                        System.out.println("\t " + String.format("%." + DECIMAL_PLACES + "f", subGroup.getEvaluation()) + ") \t" + subGroup.shortNotation());
                        System.out.println("\t " + String.format("%." + DECIMAL_PLACES + "f", subGroup2.getEvaluation()) + ") \t" + subGroup2.shortNotation());
                        System.out.println();
                    }
                }
            }
        }
        if(isFirstFirst) {
            System.out.println("No equalities detected.");
            System.out.println();
        }
    }*/
}

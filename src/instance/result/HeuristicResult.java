package instance.result;

import instance.heuristic.AbstractHeuristic;
import instance.object.Group;
import instance.object.Instance;

/**
 * Object containing the heuristic result.
 */
public class HeuristicResult {
    //Values characterizing a heuristic result.
    private final double coveredPositive;
    private final double coveredNegative;
    private final double positiveCount;
    private final double negativeCount;
    private double evaluation;

    /**
     * Create a heuristic result containing the confusion matrix.
     *
     * @param coveredPositive The p value in the confusion table.
     * @param notCoveredPositive The P - p value in the confusion table.
     * @param coveredNegative The n value in the confusion table.
     * @param notCoveredNegative The N - n value in the confusion table.
     */
    private HeuristicResult(double coveredPositive, double notCoveredPositive, double coveredNegative, double notCoveredNegative) {
        this.coveredPositive = coveredPositive;
        this.coveredNegative = coveredNegative;
        this.positiveCount = coveredPositive + notCoveredPositive;
        this.negativeCount = coveredNegative + notCoveredNegative;
    }

    /**
     * Get the p value in the confusion table.
     *
     * @return The amount of covered positive instances.
     */
    public double getCoveredPositive() {
        return coveredPositive;
    }

    /**
     * Get the n value in the confusion table.
     *
     * @return The amount of covered negative instances.
     */
    public double getCoveredNegative() {
        return coveredNegative;
    }

    /**
     * Get the P value in the confusion table.
     *
     * @return The amount of positive instances.
     */
    public double getPositiveCount() {
        return positiveCount;
    }

    /**
     * Get the N value in the confusion table.
     *
     * @return The amount of negative instances.
     */
    public double getNegativeCount() {
        return negativeCount;
    }

    /**
     * Get the evaluation value assigned to this result.
     *
     * @return A double value representing the evaluation result.
     */
    public double getEvaluationValue() {
        return evaluation;
    }

    /**
     * Set the evaluation value of this result.
     *
     * @param evaluation A double value representing the evaluation result.
     */
    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    /**
     * Get the string representation of the information contained in the heuristic result.
     *
     * @return All data that would be visible within the confusion matrix, together with the evaluation value.
     */
    @Override
    public String toString() {
        return "HeuristicResult{" +
                "p=" + coveredPositive +
                ", n=" + coveredNegative +
                ", P-p=" + (positiveCount - coveredPositive) +
                ", N-n=" + (negativeCount - coveredNegative) +
                ", p+n=" + (coveredPositive + coveredNegative) +
                ", N+P-p-n=" + (this.positiveCount + this.negativeCount - coveredPositive - coveredNegative) +
                ", P=" + positiveCount +
                ", N=" + negativeCount +
                ", N+P=" + (this.positiveCount + this.negativeCount) +
                ", evaluation=" + evaluation +
                '}';
    }

    /**
     * Evaluate the instances given the heuristic and the subgroup.
     *
     * @param heuristic The heuristic function to use.
     * @param group The subgroup to evaluate.
     * @param instances The instances within the arff file.
     * @return An object containing all evaluation information.
     */
    public static HeuristicResult evaluate(AbstractHeuristic heuristic, Group group, Instance[] instances) {
        //Get the confusion table.
        HeuristicResult result = getConfusionTable(group, instances);

        //Evaluate the confusion table.
        double evaluation = heuristic.evaluate(result.getCoveredPositive(), result.getCoveredNegative(), result.getPositiveCount(), result.getNegativeCount());
        result.setEvaluation(evaluation);
        return result;
    }

    /**
     * Get a heuristic result with the values of the confusion table set.
     *
     * @param group The group to use as cutoff.
     * @param instances The list of instances that the confusion table should be build from.
     * @return Heuristic result with the values of the confusion table set.
     */
    private static HeuristicResult getConfusionTable(Group group, Instance[] instances) {
        double coveredPositive = 0;
        double coveredNegative = 0;
        double notCoveredPositive = 0;
        double notCoveredNegative = 0;

        for(Instance instance : instances) {
            if(group.contains(instance)) {
                if(instance.getTargetValue().equals("1")) {
                    coveredPositive++;
                } else {
                    coveredNegative++;
                }
            } else {
                if(instance.getTargetValue().equals("1")) {
                    notCoveredPositive++;
                } else {
                    notCoveredNegative++;
                }
            }
        }

        return new HeuristicResult(coveredPositive, notCoveredPositive, coveredNegative, notCoveredNegative);
    }
}

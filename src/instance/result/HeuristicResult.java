package instance.result;

import instance.heuristic.AbstractHeuristic;
import instance.object.EvaluationMetric;
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
    private final double unknown;
    private double evaluation;

    /**
     * Create a heuristic result containing the confusion matrix.
     *
     * @param coveredPositive The p value in the confusion table.
     * @param positive The P value in the confusion table.
     * @param coveredNegative The n value in the confusion table.
     * @param negative The N value in the confusion table.
     */
    private HeuristicResult(double coveredPositive, double positive, double coveredNegative, double negative, double unknown) {
        this.coveredPositive = coveredPositive;
        this.coveredNegative = coveredNegative;
        this.positiveCount = positive;
        this.negativeCount = negative;
        this.unknown = unknown;
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
     * Get the amount of unknown classifications.
     *
     * @return The amount of instances we could not evaluate.
     */
    public double getUnknown() {
        return unknown;
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
                ", p+n=" + (this.coveredPositive + this.coveredNegative) +
                ", P=" + positiveCount +
                ", N=" + negativeCount +
                ", P+N=" + (this.positiveCount + this.negativeCount) +
                ", N.A.=" + unknown +
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

        //Make sure that we do not get NaN errors.
        evaluation = Double.isFinite(evaluation) ? evaluation : Integer.MIN_VALUE;

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
        double positive = 0;
        double negative = 0;
        double unknown = 0;

        for(Instance instance : instances) {
            Group.ContainsHelper containsValue = group.containsInstance(instance);

            //If we cannot evaluate the value for one of the attributes, just skip it.
            if(containsValue == Group.ContainsHelper.UNKNOWN) {
                unknown++;
            }

            //Global counting.
            if(instance.getTargetValue().equals("1")) {
                positive++;
            } else {
                negative++;
            }

            //Skip in case the value is unknown, as we will not be able to process that.
            if(containsValue == Group.ContainsHelper.TRUE) {
                if(instance.getTargetValue().equals("1")) {
                    coveredPositive++;
                } else {
                    coveredNegative++;
                }
            }
        }

        return new HeuristicResult(coveredPositive, positive, coveredNegative, negative, unknown);
    }
}

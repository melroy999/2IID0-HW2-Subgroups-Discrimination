package instance.heuristic;

/**
 * The weighted relative accuracy function, found on page 49 in:
 *
 * J. Fürnkranz, P.A. Flach
 * ROC 'n' Rule Learning - Towards a Better Understanding of Covering Algorithms, Machine Learning 58(1):39{77, 2005.
 */
public class WeightedRelativeAccuracyHeuristic extends AbstractHeuristic {
    /**
     * Evaluate the given confusion table.
     *
     * @param p The p value in the confusion table.
     * @param n The n value in the confusion table.
     * @param P The P value in the confusion table.
     * @param N The N value in the confusion table.
     * @return The result calculated by the heuristic.
     */
    @Override
    public double evaluate(double p, double n, double P, double N) {
        return ((p + n) / (P + N)) * (p / (p + n) - P / (P + N));
    }

    /**
     * Whether this heuristic counts unknowns as valid results in LEQ metric mode.
     *
     * @return Whether this heuristic counts unknowns as valid results in LEQ metric mode.
     */
    @Override
    public boolean countsUnknownsInLEQ() {
        return false;
    }

    /*@Override
    public HeuristicResult evaluate(SubGroup subGroup, Instance[] instance) {
        HeuristicResult result = getConfusionTable(subGroup, instance);

        double p = result.getCoveredPositive();
        double P = result.getPositiveCount();
        double n = result.getCoveredNegative();
        double N = result.getNegativeCount();

        double evaluation = ((p + n) / (P + N)) * (p / (p + n) - P / (P + N));

        //The heuristic function as defined in the paper on page 49.
        result.setEvaluation(evaluation);

        return result;
    }*/
}

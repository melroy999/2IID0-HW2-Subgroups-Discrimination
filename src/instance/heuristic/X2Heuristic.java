package instance.heuristic;

import instance.result.HeuristicResult;

/**
 * The χ2 statistic, found on page 60 in:
 *
 * J. Fürnkranz, P.A. Flach
 * ROC 'n' Rule Learning - Towards a Better Understanding of Covering Algorithms, Machine Learning 58(1):39{77, 2005.
 */
public class X2Heuristic extends AbstractHeuristic {

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
        return (((p * N - P * n) * (p * N - P * n)) / (P + N)) * (((P + N) * (P + N)) / (P * N * (p + n) * (P + N - p - n)));
    }

    /**
     * Whether this heuristic counts unknowns as valid results in LEQ metric mode.
     *
     * @return Whether this heuristic counts unknowns as valid results in LEQ metric mode.
     */
    @Override
    public boolean countsUnknownsInLEQ() {
        return true;
    }
}

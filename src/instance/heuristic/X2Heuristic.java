package instance.heuristic;

import instance.Instance;
import instance.result.EvaluationResult;
import instance.search.SubGroup;

public class X2Heuristic extends AbstractHeuristic {
    @Override
    public EvaluationResult evaluate(SubGroup subGroup, Instance[] instance) {
        EvaluationResult result = getConfusionTable(subGroup, instance);

        double p = result.getCoveredPositive();
        double P = result.getPositiveCount();
        double n = result.getCoveredNegative();
        double N = result.getNegativeCount();

        double evaluation = (((p * N - P * n) * (p * N - P * n)) / (P + N)) * ((P + N) * (P + N) / (P * N * (p + n) * (P + N - p - n)));

        //The sensitivity quality measure.
        result.setEvaluation(evaluation);

        return result;
    }
}

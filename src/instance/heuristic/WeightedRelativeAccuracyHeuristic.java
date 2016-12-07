package instance.heuristic;

import instance.Instance;
import instance.result.EvaluationResult;
import instance.search.SubGroup;

import java.util.HashSet;

public class WeightedRelativeAccuracyHeuristic extends AbstractHeuristic {

    @Override
    public EvaluationResult evaluate(SubGroup subGroup, Instance[] instance) {
        EvaluationResult result = getConfusionTable(subGroup, instance);

        double p = result.getCoveredPositive();
        double P = result.getPositiveCount();
        double n = result.getCoveredNegative();
        double N = result.getNegativeCount();

        double evaluation = ((p + n) / (P + N)) * (p / (p + n) - P / (P + N));

        //The heuristic function as defined in the paper on page 49.
        result.setEvaluation(evaluation);

        return result;
    }
}
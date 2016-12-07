package instance.heuristic;

import instance.Instance;
import instance.result.EvaluationResult;
import instance.search.SubGroup;

public class SpecificityQualityMeasureHeuristic extends AbstractHeuristic {
    @Override
    public EvaluationResult evaluate(SubGroup subGroup, Instance[] instance) {
        EvaluationResult result = getConfusionTable(subGroup, instance);

        double n = result.getCoveredNegative();
        double N = result.getNegativeCount();

        double evaluation = 1 - n / N;

        //The sensitivity quality measure.
        result.setEvaluation(evaluation);

        //The specificity quality measure.
        result.setEvaluation(
                1 - result.getCoveredNegative() / result.getNegativeCount()
        );

        return result;
    }
}

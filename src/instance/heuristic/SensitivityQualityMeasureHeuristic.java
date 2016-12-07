package instance.heuristic;

import instance.Instance;
import instance.result.EvaluationResult;
import instance.search.SubGroup;

public class SensitivityQualityMeasureHeuristic extends AbstractHeuristic {
    @Override
    public EvaluationResult evaluate(SubGroup subGroup, Instance[] instance) {
        EvaluationResult result = getConfusionTable(subGroup, instance);

        double p = result.getCoveredPositive();
        double P = result.getPositiveCount();

        double evaluation = p / P;

        //The sensitivity quality measure.
        result.setEvaluation(evaluation);

        return result;
    }
}

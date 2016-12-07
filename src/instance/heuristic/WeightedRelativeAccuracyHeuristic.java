package instance.heuristic;

import instance.Instance;
import instance.result.EvaluationResult;
import instance.search.SubGroup;

import java.util.HashSet;

public class WeightedRelativeAccuracyHeuristic extends AbstractHeuristic {

    @Override
    public EvaluationResult evaluate(HashSet<SubGroup> seeds, SubGroup subGroup, Instance[] instance) {
        EvaluationResult result = getConfusionTable(seeds, subGroup, instance);

        //The heuristic function as defined in the paper on page 49.
        result.setEvaluation(
                (result.getCoveredInstancesCount() / result.getTotalInstances()) *
                (
                    (result.getCoveredPositive() / result.getCoveredInstancesCount()) -
                    (result.getPositiveCount() / result.getTotalInstances())
                )
        );

        return result;
    }
}

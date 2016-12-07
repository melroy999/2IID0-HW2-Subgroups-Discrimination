package instance.heuristic;

import instance.Instance;
import instance.result.EvaluationResult;
import instance.search.SubGroup;

import java.util.HashSet;

public abstract class AbstractHeuristic {
    public abstract EvaluationResult evaluate(SubGroup subGroup, Instance[] instance);

    protected EvaluationResult getConfusionTable(SubGroup subGroup, Instance[] instances) {
        double coveredPositive = 0;
        double coveredNegative = 0;
        double notCoveredPositive = 0;
        double notCoveredNegative = 0;

        for(Instance instance : instances) {
            if(subGroup.isPartOfSubgroup(instance)) {
                if(instance.getTarget().equals("1")) {
                    coveredPositive++;
                } else {
                    coveredNegative++;
                }
            } else {
                if(instance.getTarget().equals("1")) {
                    notCoveredPositive++;
                } else {
                    notCoveredNegative++;
                }
            }
        }

        return new EvaluationResult(coveredPositive, notCoveredPositive, coveredNegative, notCoveredNegative);
    }
}

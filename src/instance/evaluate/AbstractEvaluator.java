package instance.evaluate;

import instance.Instance;
import instance.result.EvaluationResult;
import instance.search.SubGroup;

import java.util.HashSet;

public abstract class AbstractEvaluator {
    public abstract EvaluationResult evaluate(HashSet<SubGroup> seeds, SubGroup subGroup, Instance[] instance);

    protected boolean isPartOfSubgroups(HashSet<SubGroup> seeds, SubGroup currentSubGroup, Instance instance) {
        boolean result = currentSubGroup.isPartOfSubgroup(instance);

        for(SubGroup subGroup : seeds) {
            if(!result) {
                break;
            }

            //Already checked for true, so can forget previous value of result.
            result = subGroup.isPartOfSubgroup(instance);
        }

        return result;
    }

    protected EvaluationResult getConfusionTable(HashSet<SubGroup> seeds, SubGroup subGroup, Instance[] instances) {
        double positive = 0;
        double negative = 0;
        double falsePositive = 0;
        double falseNegative = 0;
        int instancesInSubgroup = 0;
        for(Instance instance : instances) {
            if(isPartOfSubgroups(seeds, subGroup, instance)) {
                instancesInSubgroup++;
                if(instance.getTarget().equals("1")) {
                    positive++;
                } else {
                    falsePositive++;
                }
            } else {
                if(instance.getTarget().equals("1")) {
                    falseNegative++;
                } else {
                    negative++;
                }
            }
        }

        return new EvaluationResult(positive, negative, falsePositive, falseNegative, instancesInSubgroup);
    }
}

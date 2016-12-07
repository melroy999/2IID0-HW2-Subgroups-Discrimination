package instance.evaluate;

import instance.Instance;
import instance.result.EvaluationResult;
import instance.search.SubGroup;

import java.util.HashSet;

public class SimpleEvaluator extends AbstractEvaluator {

    @Override
    public EvaluationResult evaluate(HashSet<SubGroup> seeds, SubGroup subGroup, Instance[] instance) {
        EvaluationResult result = getConfusionTable(seeds, subGroup, instance);

        result.setEvaluation(result.getSum());

        return result;
    }
}

package instance.search;

import instance.ArffFile;
import instance.Instance;
import instance.attribute.AbstractAttribute;
import instance.evaluate.AbstractEvaluator;
import instance.result.EvaluationResult;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class BeamSearch {
    public static HashSet<SubGroup> search(ArffFile data, AbstractEvaluator evaluator, int searchDepth, int searchWidth, HashSet<String> blackListed) {

        SubGroup bestSubGroup = null;
        HashSet<SubGroup> seeds = new LinkedHashSet<>();
        HashSet<AbstractAttribute> seedAttributes = new HashSet<>();
        AbstractAttribute bestAttribute = null;

        for(int i = 0; i < searchDepth; i++) {
            double bestEvaluation = Double.MIN_VALUE;

            System.out.println("> Searching level " + i);

            for(AbstractAttribute attribute : data.getAttributes()) {
                if(seedAttributes.contains(attribute) || blackListed.contains(attribute.getName())) {
                    System.out.println("Already evaluated attribute " + attribute);
                    continue;
                }

                //We don't want to rank the target, only the descriptors.
                if(attribute.getId() < data.getAttributes().length - 1) {
                    SubGroup subGroup = searchOnAttribute(data, evaluator, attribute, seeds);

                    //Check if we have a better one.
                    if(subGroup.getEvaluation().getEvaluation() > bestEvaluation) {
                        bestEvaluation = subGroup.getEvaluation().getEvaluation();
                        bestSubGroup = subGroup;
                        bestAttribute = attribute;
                    }
                }
            }

            System.out.println(bestSubGroup);

            seeds.add(bestSubGroup);
            seedAttributes.add(bestAttribute);
        }


        return seeds;
    }

    private static SubGroup searchOnAttribute(ArffFile data, AbstractEvaluator evaluator, AbstractAttribute attribute, HashSet<SubGroup> seeds) {
        //Hold which values have already been considered during the search.
        HashSet<String> visited = new HashSet<>();
        double bestEvaluation = Integer.MIN_VALUE;
        SubGroup bestSubGroup = null;

        for(Instance subGroupCandidates : data.getInstances()) {
            String value = subGroupCandidates.getValue(attribute);

            //Skip when subGroup has already been observed, or when the value is unknown.
            if(value.equals("?") || visited.contains(value)) {
                //Skip
                continue;
            } else {
                //Add it to the set.
                visited.add(value);
            }

            //Create a subgroup for this cutoff value.
            SubGroup subGroup = new SubGroup(attribute, value);

            //Evaluate this subgroup, by incrementing for instances in subgroup with positive target,
            //and decrement for instances not in subgroup with positive target.
            EvaluationResult evaluation = evaluator.evaluate(seeds, subGroup, data.getInstances());

            //Set the evaluation for the subgroup.
            subGroup.setEvaluation(evaluation);

            //Check if we have a better one.
            if(evaluation.getEvaluation() > bestEvaluation) {
                bestEvaluation = evaluation.getEvaluation();
                bestSubGroup = subGroup;
            }
        }

        return bestSubGroup;
    }
}

package instance.search;

import instance.ArffFile;
import instance.Instance;
import instance.attribute.AbstractAttribute;

import java.util.HashSet;

public class BeamSearch {
    public static SubGroup search(ArffFile data, int searchWidth, int searchDepth) {
        int bestEvaluation = Integer.MIN_VALUE;
        SubGroup bestSubGroup = null;

        for(AbstractAttribute attribute : data.getAttributes()) {
            System.out.println("Evaluating attribute " + attribute);

            //We don't want to rank the target, only the descriptors.
            if(attribute.getId() < data.getAttributes().length - 1) {
                SubGroup bestAttributeSubgroup = searchOnAttribute(data, attribute);

                //Check if we have a better one.
                if(bestAttributeSubgroup.getEvaluation() > bestEvaluation) {
                    bestEvaluation = bestAttributeSubgroup.getEvaluation();
                    bestSubGroup = bestAttributeSubgroup;
                }
            }
        }

        return bestSubGroup;
    }

    private static SubGroup searchOnAttribute(ArffFile data, AbstractAttribute attribute) {
        //Hold which values have already been considered during the search.
        HashSet<String> visited = new HashSet<>();
        int bestEvaluation = Integer.MIN_VALUE;
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
            int evaluation = 0;
            for(Instance instance : data.getInstances()) {
                if(subGroup.isPartOfSubgroup(instance)) {
                    evaluation += (instance.getTarget().equals("1") ? 1 : 0);
                } else {
                    evaluation += (instance.getTarget().equals("1") ? -1 : 0);
                }
            }

            //Set the evaluation for the subgroup.
            subGroup.setEvaluation(evaluation);

            //Check if we have a better one.
            if(evaluation > bestEvaluation) {
                bestEvaluation = evaluation;
                bestSubGroup = subGroup;
            }
        }

        return bestSubGroup;
    }
}

package instance.search;

import instance.ArffFile;
import instance.Instance;
import instance.attribute.AbstractAttribute;
import instance.heuristic.AbstractHeuristic;
import instance.result.EvaluationResult;

import java.util.*;

public class BeamSearch {
    public static SubGroup[][] search(ArffFile data, AbstractHeuristic evaluator, int searchDepth, int searchWidth, HashSet<String> blackListed) {
        System.out.println("Performing beam search with search depth " + searchDepth + " and search width " + searchWidth + ".");
        HashSet<SubGroup> seeds = new HashSet<>();

        //Save the results for each depth.
        SubGroup[][] resultMap = new SubGroup[searchDepth][searchWidth];
        HashSet<SubGroup> bestGroups = new HashSet<>();

        for(int level = 0; level < searchDepth; level++) {
            for(AbstractAttribute attribute : data.getAttributes()) {
                if(blackListed.contains(attribute.getName())) {
                    continue;
                }

                //We don't want to rank the target, only the descriptors.
                if(attribute.getId() < data.getAttributes().length - 1) {
                    HashSet<SubGroup> bestSubGroups = searchOnAttribute(data, evaluator, attribute, seeds, searchWidth);

                    for(SubGroup subGroup : bestSubGroups) {
                        //Check if we have space in the best result list, otherwise remove.
                        if(bestGroups.size() < searchWidth) {
                            bestGroups.add(subGroup);
                        } else {
                            double worstEvaluationValue = Double.MAX_VALUE;
                            SubGroup worstSubgroup = null;

                            for(SubGroup sg : bestGroups) {
                                double eval = sg.getHeuristic().getEvaluation();
                                if(eval < worstEvaluationValue) {
                                    worstEvaluationValue = eval;
                                    worstSubgroup = sg;
                                }
                            }

                            bestGroups.remove(worstSubgroup);
                            bestGroups.add(subGroup);
                        }
                    }
                }
            }

            int index = 0;
            for(SubGroup subGroup : bestGroups) {
                resultMap[level][index] = subGroup;
                index++;
            }
            Arrays.sort(resultMap[level]);

            //Set the new seeds.
            seeds = new HashSet<>(Arrays.asList(resultMap[level]));
        }

        return resultMap;
    }

    private static HashSet<SubGroup> searchOnAttribute(ArffFile data, AbstractHeuristic evaluator, AbstractAttribute attribute, HashSet<SubGroup> seeds, int searchWidth) {
        //Hold which values have already been considered during the search.
        HashSet<String> visited = new HashSet<>();
        HashSet<SubGroup> bestGroups = new HashSet<>();

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

            SubGroup[] seededGroups;
            if(seeds.size() == 0) {
                seededGroups = new SubGroup[]{new SubGroup(attribute, value)};
            } else {
                seededGroups = new SubGroup[seeds.size()];

                int i = 0;
                for(SubGroup seed : seeds) {
                    seededGroups[i] = new SubGroup(attribute, value, seed);
                    i++;
                }
            }

            for(SubGroup subGroup : seededGroups) {
                //If the subgroup contains duplicates, skip.
                if(subGroup.getSubGroup() != null && subGroup.getSubGroup().hasSimilarSubgroup(attribute, value)) {
                    continue;
                }

                //Evaluate this subgroup, by incrementing for instances in subgroup with positive target,
                //and decrement for instances not in subgroup with positive target.
                EvaluationResult evaluation = evaluator.evaluate(subGroup, data.getInstances());

                //Check whether the subgroup is actually a subgroup, and not the complete set.
                if(evaluation.getCoveredPositive() + evaluation.getCoveredNegative() == evaluation.getPositiveCount() + evaluation.getNegativeCount()) {
                    continue;
                }

                //Check if the length of this set is different from the subset.
                if(subGroup.getSubGroup() != null) {
                    EvaluationResult subGroupEvaluation = subGroup.getSubGroup().getHeuristic();
                    //Check if we have equal counts.
                    if(evaluation.getCoveredPositive() == subGroupEvaluation.getCoveredPositive() && evaluation.getCoveredNegative() == subGroupEvaluation.getCoveredNegative()) {
                        continue;
                    }
                }

                //Make sure we do not get NaN or the sorts.
                if(!Double.isFinite(evaluation.getEvaluation())) {
                    evaluation.setEvaluation(-1);
                }

                //Set the evaluation for the subgroup.
                subGroup.setEvaluation(evaluation);

                //Check if we have space in the best result list, otherwise remove.
                if(bestGroups.size() < searchWidth) {
                    bestGroups.add(subGroup);
                } else {
                    double worstEvaluationValue = Double.MAX_VALUE;
                    SubGroup worstSubgroup = null;

                    for(SubGroup sg : bestGroups) {
                        double eval = sg.getHeuristic().getEvaluation();
                        if(eval < worstEvaluationValue) {
                            worstEvaluationValue = sg.getHeuristic().getEvaluation();
                            worstSubgroup = sg;
                        }
                    }

                    bestGroups.remove(worstSubgroup);
                    bestGroups.add(subGroup);
                }
            }
        }

        return bestGroups;
    }
}

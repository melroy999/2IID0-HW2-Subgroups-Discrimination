package instance.search;

import instance.ArffFile;
import instance.Instance;
import instance.attribute.AbstractAttribute;
import instance.heuristic.AbstractHeuristic;
import instance.result.EvaluationResult;

import java.text.SimpleDateFormat;
import java.util.*;

public class BeamSearch {

    public static SubGroup[][] search(ArffFile data, AbstractHeuristic evaluator, int searchDepth, int searchWidth, HashSet<String> blackListed, boolean allowSubgroupWithDifferentValues) {
        System.out.println("[" + getCurrentTimeStamp() + "]: Performing beam search with search depth " + searchDepth + " and search width " + searchWidth + ".");
        HashSet<SubGroup> seeds = new HashSet<>();

        //Save the results for each depth.
        SubGroup[][] resultMap = new SubGroup[searchDepth][searchWidth];
        HashSet<SubGroup> bestGroups = new HashSet<>();

        System.out.print("Current level: ");
        for(int level = 0; level < searchDepth; level++) {
            System.out.print((level + 1) + "... ");
            for(AbstractAttribute attribute : data.getAttributes()) {
                if(blackListed.contains(attribute.getName())) {
                    continue;
                }

                //We don't want to rank the target, only the descriptors.
                if(attribute.getId() < data.getAttributes().length - 1) {
                    HashSet<SubGroup> bestSubGroups = searchOnAttribute(data, evaluator, attribute, seeds, searchWidth, allowSubgroupWithDifferentValues);

                    subGroupLoop: for(SubGroup subGroup : bestSubGroups) {
                        //Check whether one of the subgroups in the best groups list already contains ALL of the attributes, with the exact same values.
                        for(SubGroup sg : bestGroups) {
                            if(sg.recursiveHasAllSubgroups(subGroup, allowSubgroupWithDifferentValues)) {
                                continue subGroupLoop;
                            }
                        }

                        //Check if we have space in the best result list, otherwise remove.
                        if(bestGroups.size() < searchWidth) {
                            bestGroups.add(subGroup);
                        } else {
                            double worstEvaluationValue = Double.MAX_VALUE;
                            SubGroup worstSubgroup = null;

                            for(SubGroup sg : bestGroups) {
                                double eval = sg.getHeuristic().getEvaluationValue();
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
        System.out.println();

        return resultMap;
    }

    private static HashSet<SubGroup> searchOnAttribute(ArffFile data, AbstractHeuristic evaluator, AbstractAttribute attribute, HashSet<SubGroup> seeds, int searchWidth, boolean allowSubgroupWithDifferentValues) {
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

            subGroupLoop: for(SubGroup subGroup : seededGroups) {
                //If the subgroup of this subgroup already contains the subgroup we want to observe, skip.
                if(subGroup.getSubGroup() != null && subGroup.getSubGroup().recursiveHasAttribute(attribute)) {
                    continue;
                }

                //Evaluate this subgroup.
                EvaluationResult evaluation = evaluator.evaluate(subGroup, data.getInstances());

                //Make sure we do not get NaN or the sorts.
                if(!Double.isFinite(evaluation.getEvaluationValue())) {
                    evaluation.setEvaluation(-1);
                }

                //Set the evaluation for the subgroup.
                subGroup.setEvaluation(evaluation);

                //Check whether the subgroup is actually a subgroup, and not the complete set.
                if(evaluation.getCoveredPositive() + evaluation.getCoveredNegative() == evaluation.getPositiveCount() + evaluation.getNegativeCount()) {
                    continue;
                }

                //Check whether one of the seeds already contains ALL of the attributes, with the exact same values.
                for(SubGroup sg : seeds) {
                    if(sg.recursiveHasAllSubgroups(subGroup, allowSubgroupWithDifferentValues)) {
                        continue subGroupLoop;
                    }
                }

                SubGroup toReplace = null;
                //Check whether one of the subgroups in the best groups list already contains ALL of the attributes, with the exact same values.
                for(SubGroup sg : bestGroups) {
                    if(sg.recursiveHasAllSubgroups(subGroup, allowSubgroupWithDifferentValues)) {
                        //Check whether we improve that specific subgroup by doing this. If so, replace it.
                        if(evaluation.getEvaluationValue() > sg.getEvaluation()) {
                            toReplace = sg;
                            break;
                        } else {
                            continue subGroupLoop;
                        }
                    }
                }

                //Replace in subgroup.
                if(toReplace != null) {
                    bestGroups.remove(toReplace);
                    bestGroups.add(subGroup);
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

                //Check if we have space in the best result list, otherwise remove.
                if(bestGroups.size() < searchWidth) {
                    bestGroups.add(subGroup);
                } else {
                    double worstEvaluationValue = Double.MAX_VALUE;
                    SubGroup worstSubgroup = null;

                    for(SubGroup sg : bestGroups) {
                        double eval = sg.getHeuristic().getEvaluationValue();
                        if(eval < worstEvaluationValue) {
                            worstEvaluationValue = sg.getHeuristic().getEvaluationValue();
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

    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }
}

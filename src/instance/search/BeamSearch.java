package instance.search;

import instance.attribute.AbstractAttribute;
import instance.heuristic.AbstractHeuristic;
import instance.object.*;
import instance.result.HeuristicResult;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class used for beam searches.
 */
public class BeamSearch {
    public static Group[][] search(ArffFile data, AbstractHeuristic heuristic, int searchWidth, int searchDepth, boolean checkValue, HashSet<String> blacklist) {
        //Create the array that will contain all intermediary results.
        Group[][] result = new Group[searchDepth][];

        //The list containing the best groups found.
        FixedSizeGroupCollection bestGroups = new FixedSizeGroupCollection(searchWidth, checkValue);

        //For each level.
        for(int level = 0; level < searchDepth; level++) {
            System.out.println("[" + getCurrentTimeStamp() + "]: Current level: " + (level + 1));

            //A new beam, which will later be merged with the bestGroups one.
            FixedSizeGroupCollection attributeBestGroups = new FixedSizeGroupCollection(searchWidth, checkValue);

            //For each attribute.
            for(AbstractAttribute attribute : data.getAttributes()) {
                //Skip those attributes we don't care about.
                if (blacklist.contains(attribute.getName())) {
                    continue;
                }

                //Skip the target.
                if(attribute.getId() != data.getTarget()) {
                    //Get the collection of best results for the group.
                    attributeSearch(data, heuristic, attribute, bestGroups.toArray(), attributeBestGroups);
                }
            }

            //Add the results of this level's results to the best groups list.
            for(Group group : attributeBestGroups.toArray()) {
                bestGroups.add(group);
            }


            //Add the level's results to the result array.
            result[level] = bestGroups.toArray();
        }

        return result;
    }

    /**
     * Search on a fixed attribute.
     *
     * @param data The data set.
     * @param heuristic The heuristic function to use.
     * @param attribute The attribute we want to check values of.
     * @param seeds The list of seeds the search should use.
     * @return A collection of best results.
     */
    public static void attributeSearch(ArffFile data, AbstractHeuristic heuristic, AbstractAttribute attribute, Group[] seeds, FixedSizeGroupCollection bestGroups) {
        //List of values we have seen already.
        HashSet<String> visitedCombinations = new HashSet<>();

        //Iterate over all instances, and get the value corresponding to the selected attribute.
        for(Instance instance : data.getInstances()) {
            String value = instance.getValue(attribute);

            //We can do this check before the metric stuff, as if the value is present, we know we have ran all the metrics before anyway.
            if(visitedCombinations.contains(value) || value.equals("?")) {
                continue;
            }

            //Add the value to the visited list.
            visitedCombinations.add(value);

            //We have different options for the metric.
            for(EvaluationMetric metric : EvaluationMetric.getValues(attribute.getType())) {

                //As we have a list of seeds, we have to iterate over all of them as well during the result gathering.
                Group[] seededGroups;
                if (seeds.length == 0) {
                    seededGroups = new Group[]{new Group(attribute, value, metric)};
                } else {
                    seededGroups = new Group[seeds.length];
                    int i = 0;
                    for (Group seed : seeds) {
                        seededGroups[i] = new Group(attribute, value, metric, seed);
                        i++;
                    }
                }

                //Iterate over all seeded groups.
                for(Group group : seededGroups) {
                    //With different metrics, a group could appear more often than once.
                    //The group should not have two or more metrics on the same value.
                    if(group.getSeed() != null && group.getSeed().containsGroup(attribute, metric)) {
                        continue;
                    }

                    //Do an evaluation.
                    HeuristicResult result = HeuristicResult.evaluate(heuristic, group, data.getInstances());

                    //Set the result in the group itself.
                    group.setResult(result);

                    //Check if the group has reduced the size of the seed, or the size of the total set, depending on the value of seed.
                    if(group.getSeed() == null) {
                        //Check if we have a different set than the original data set. Make sure that our group is not empty...
                        if(result.getCoveredPositive() + result.getCoveredNegative() == result.getPositiveCount() + result.getNegativeCount() || result.getCoveredPositive() + result.getCoveredNegative() == 0) {
                            continue;
                        }
                    } else {
                        //Check if this set is different from the seed's set.
                        if(result.getCoveredPositive() == result.getPositiveCount() && result.getCoveredNegative() == result.getNegativeCount()) {
                            continue;
                        }
                    }

                    //Add the group to the candidacy list.
                    bestGroups.add(group);
                }
            }
        }
    }

    /**
     * Get the current time stamp string.
     *
     * @return Time stamp in string representation.
     */
    public static String getCurrentTimeStamp() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }
}

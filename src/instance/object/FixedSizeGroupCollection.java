package instance.object;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 * Create a fixed size group collection, that will keep the specified number of best results.
 */
public class FixedSizeGroupCollection {
    private final TreeSet<Group> bestGroups = new TreeSet<>();
    private final boolean checkValue;
    private final int size;
    private double worstResult;

    /**
     * Create a data structure that used a fixed size sorted model.
     *
     * @param checkValue Check if the values are equal or not, next to the attributes.
     */
    public FixedSizeGroupCollection(int size, boolean checkValue) {
        this.checkValue = checkValue;
        this.size = size;
    }

    /**
     * Add the group to the best groups if applicable.
     *
     * @param candidate The candidate group to add.
     */
    public void add(Group candidate) {
        //Check if the value is worth adding, whenever the list is full.
        if(bestGroups.size() == size) {
            if(candidate.compareTo(bestGroups.last()) != -1) {
                //Not worth my time.
                return;
            }
        }

        //The action we want to take.
        Action action = Action.ADD;
        Group replace = null;

        //Check whether we have something similar.
        for(Group group : bestGroups) {
            //We don't want the subgroup to be an exact duplicate to one of the groups in here.
            if(candidate.isDuplicateOf(group, true, false)) {
                //Abort.
                action = Action.ABORT;
                break;
            }

            if(!checkValue && candidate.getResult().getEvaluationValue() == group.getResult().getEvaluationValue() && candidate.isMoreSpecificThan(group)) {
                replace = group;
                action = Action.REPLACE;
                break;
            }
        }

        //Based on the action, do what needs to be done.
        switch (action) {
            case ABORT:
                return;
            case REPLACE:
                //Replace the worst element.
                bestGroups.remove(replace);
                bestGroups.add(candidate);
                break;
            default:
                //Add it to the list.
                if(bestGroups.size() < size) {
                    //Just add it.
                    bestGroups.add(candidate);
                } else {
                    //Remove the worst performing, and add the candidate.
                    bestGroups.pollLast();
                    bestGroups.add(candidate);
                }
        }

        worstResult = bestGroups.last().getResult().getEvaluationValue();
    }

    /**
     * Get the worst value in this list.
     *
     * @return The worst performance value.
     */
    public double getWorstResult() {
        return worstResult;
    }

    /**
     * Helper enum class.
     */
    private enum Action {
        ADD, ABORT, REPLACE
    }

    /**
     * Convert the tree set to an array.
     *
     * @return The treeset as an array.
     */
    public Group[] toArray() {
        return bestGroups.toArray(new Group[bestGroups.size()]);
    }
}

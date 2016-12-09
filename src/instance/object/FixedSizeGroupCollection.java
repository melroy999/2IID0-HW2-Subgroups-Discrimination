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
    public synchronized void add(Group candidate) {
        //Check if the value is worth adding, whenever the list is full.
        if(bestGroups.size() == size) {
            if(worstResult > candidate.getResult().getEvaluationValue()) {
                //Not worth my time.
                return;
            }
        }

        //The action we want to take.
        Action action = Action.ADD;
        Group replace = null;

        //Check whether we have something similar.
        for(Group group : bestGroups) {
            //Check if this candidate can potentially improve the selected group.
            boolean isImprovement = candidate.improvesGroup(candidate);

            //Check if we have a permutation of one of the best groups.
            if(candidate.isDuplicateOf(group, checkValue, false)) {
                action = Action.ABORT;

                //If we have a duplicate, we do not want this candidate in the list.
                //However, if checkValue is false, it could be that this is an improvement. So replace in that case.
                if(!checkValue && isImprovement) {
                    action = Action.REPLACE;
                    replace = group;
                }

                //Break the loop regardless of the case.
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
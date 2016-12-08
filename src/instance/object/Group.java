package instance.object;

import instance.attribute.AbstractAttribute;
import instance.result.HeuristicResult;

/**
 * Object representing a (sub)group.
 */
public class Group {
    //Values that characterize a group.
    private final AbstractAttribute attribute;
    private final String value;
    private final EvaluationMetric metric;

    //The seed this group uses, if null > no seed used.
    private final Group seed;

    //The evaluation scores for this specific group.
    private HeuristicResult result;

    //How many seeds this group has recursively.
    private final int seeds;

    /**
     * Create a group without a seed, given its characteristics.
     *
     * @param attribute The attribute the group is based on.
     * @param value The cutoff value to use.
     * @param metric The comparison mode to use, which is ==, <= or >=.
     */
    public Group(AbstractAttribute attribute, String value, EvaluationMetric metric) {
        this(attribute, value, metric, null);
    }

    /**
     * Create a group with a seed, given its characteristics.
     *
     * @param attribute The attribute the group is based on.
     * @param value The cutoff value to use.
     * @param metric The comparison mode to use, which is ==, <= or >=.
     * @param seed The group used as a seed.
     */
    public Group(AbstractAttribute attribute, String value, EvaluationMetric metric, Group seed) {
        if(metric != EvaluationMetric.EQ && attribute.getType() != Type.NUMERIC) {
            throw new IllegalArgumentException("Only the NUMERIC type supports the GTEQ or LTEQ metrics.");
        }
        this.attribute = attribute;
        this.value = value;
        this.metric = metric;
        this.seed = seed;
        this.seeds = seed == null ? 0 : seed.seeds + 1;
    }

    /**
     * Get the attribute this group uses for its cutoff process.
     *
     * @return The attribute.
     */
    public AbstractAttribute getAttribute() {
        return attribute;
    }

    /**
     * Get the cutoff value this group uses.
     *
     * @return The cutoff value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Get the evaluation metric this group uses for its cutoff process.
     *
     * @return The evaluation metric.
     */
    public EvaluationMetric getMetric() {
        return metric;
    }

    /**
     * Get the heuristic result for this specific group with its seeds.
     * @return The heuristic result, null if not set yet.
     */
    public HeuristicResult getResult() {
        return result;
    }

    /**
     * Set the result for this specific group with its seeds.
     *
     * @param result The heuristic result.
     */
    public void setResult(HeuristicResult result) {
        this.result = result;
    }

    /**
     * Get the group that seeds this group.
     *
     * @return The group that seeds this group, which is {@code null} if it does not exist.
     */
    public Group getSeed() {
        return seed;
    }

    /**
     * Check if the instance belongs to the group, and the seed.
     *
     * @param instance The instance to evaluate.
     * @return True if the instance's attribute value is in the correct range of this group and the seed, false otherwise.
     */
    public boolean contains(Instance instance) {
        return metric.compare(instance.getValue(attribute), value) && (seed == null || seed.contains(instance));
    }

    /**
     * Whether the given group improves this group.
     *
     * @param group The group that is a candidate improvement.
     * @return Whether {@code group} is an improvement or not.
     */
    public boolean improvesGroupCollection(Group group) {
        //Check whether the group gives a better evaluation or not.
        return this.getResult().getEvaluationValue() < group.getResult().getEvaluationValue();
    }

    /**
     * Check whether this group is a duplicate of the given group.
     *
     * @param group The group that is potentially duplicated.
     * @param checkValue Check if the values are equal or not, next to the attributes.
     * @return Whether the group is a duplicate or not.
     */
    public boolean isDuplicateOf(Group group, boolean checkValue) {
        return isDuplicateOf(group, checkValue, false);
    }

    /**
     * Check whether this group is a duplicate of the given group.
     *
     * @param group The group that is potentially duplicated.
     * @param checkValue Check if the values are equal or not, next to the attributes.
     * @param skipSizeCheck Check whether we want to skip the size check.
     * @return Whether the group is a duplicate or not.
     */
    private boolean isDuplicateOf(Group group, boolean checkValue, boolean skipSizeCheck) {
        //If we do not have the same amount of seeds, we cannot be duplicates.
        //We have to skip this in further recursive steps, as we will reduce the size of this group.
        if(skipSizeCheck || this.seeds != group.seeds) {
            return false;
        }

        //Check whether we have an attribute/value pair if applicable that is not present within the other group.
        //This should be especially fast recursively, as new values are often introduced in the original group, and should be detected first.
        boolean foundMatch = false;
        Group seed = group;
        while(seed != null && !foundMatch) {
            if(this.attribute.getId() == seed.attribute.getId() && (!checkValue || this.value.equals(group.value))) {
                foundMatch = true;
            }
            seed = seed.getSeed();
        }

        //If we are doing our last comparison.
        if(this.getSeed() == null) {
            return foundMatch;
        } else if(foundMatch) {
            //If a match has been found for this subgroup, check the next level.
            return isDuplicateOf(group, checkValue, true);
        } else {
            //We have found something that did not match, so it cannot be a duplicate.
            return false;
        }
    }

    /**
     * Get a human readable string denoting the kind of subgroup this is.
     *
     * @return String of the format "name metric value".
     */
    public String toSimpleString() {
        return attribute.getName() + " " + metric + " " + value;
    }

    /**
     * Convert the group to a string.
     * @return The group itself, with the seeds appended with ∧ signs.
     */
    @Override
    public String toString() {
        String result = this.toSimpleString();
        Group seed = this.seed;
        while(seed != null) {
            result += " \u2227 " + seed;
        }
        return result;
    }
}

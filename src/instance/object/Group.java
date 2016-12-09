package instance.object;

import instance.attribute.AbstractAttribute;
import instance.result.HeuristicResult;

import java.lang.reflect.Member;

/**
 * Object representing a (sub)group.
 */
public class Group implements Comparable<Group> {
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
    public boolean containsInstance(Instance instance) {
        //Skip if value is ?
        return !instance.getValue(attribute).equals("?") && metric.compare(instance.getValue(attribute), value) && (seed == null || seed.containsInstance(instance));
    }

    /**
     * Check if the attribute is already included in this group, with the same metric!
     *
     * @param attribute The attribute we want to find.
     * @param metric The metric that is used.
     * @return True if the attribute is present in this or one of the seeds.
     */
    public boolean containsGroup(AbstractAttribute attribute, EvaluationMetric metric) {
        return this.attribute.getId() == attribute.getId() && this.metric == metric || (seed != null && seed.containsGroup(attribute, metric));
    }

    /**
     * Whether the given group improves this group.
     *
     * @param group The group that is a candidate improvement.
     * @return Whether {@code group} is an improvement or not.
     */
    public boolean improvesGroup(Group group) {
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
    public boolean isDuplicateOf(Group group, boolean checkValue, boolean debug) {
        if(debug) System.out.println("Checking duplicate for " + this + " and " + group);
        return isDuplicateOf(group, checkValue, false, debug);
    }

    /**
     * Check whether this group is a duplicate of the given group.
     *
     * @param group The group that is potentially duplicated.
     * @param checkValue Check if the values are equal or not, next to the attributes.
     * @param skipSizeCheck Check whether we want to skip the size check.
     * @return Whether the group is a duplicate or not.
     */
    private boolean isDuplicateOf(Group group, boolean checkValue, boolean skipSizeCheck, boolean debug) {
        //If we do not have the same amount of seeds, we cannot be duplicates.
        //We have to skip this in further recursive steps, as we will reduce the size of this group.
        if(!skipSizeCheck && this.seeds != group.seeds) {
            return false;
        }

        //Check whether we have an attribute/value pair if applicable that is not present within the other group.
        //This should be especially fast recursively, as new values are often introduced in the original group, and should be detected first.
        boolean foundMatch = false;
        Group groupSeed = group;
        while(groupSeed != null && !foundMatch) {
            if(this.attribute.getId() == groupSeed.attribute.getId() && this.metric == groupSeed.metric && (!checkValue || this.value.equals(groupSeed.value))) {
                if(debug) System.out.println("\t\t Match between " + this.toSimpleString() + " and " + groupSeed);
                foundMatch = true;
            }
            groupSeed = groupSeed.getSeed();
        }

        if(debug && !foundMatch) System.out.println("\t\t No match between " + this.toSimpleString() + " and " + group);
        if(debug && this.getSeed() == null && foundMatch) System.out.println("\t\t Group " + group + " is duplicated");

        //If we found no match, this cannot be a duplicate.
        //If a match has been found for this subgroup, check the next level.
        return foundMatch && (this.getSeed() == null || this.getSeed().isDuplicateOf(group, checkValue, true, debug));
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
        Group seed = this.getSeed();
        while(seed != null) {
            result += " \u2227 " + seed.toSimpleString();
            seed = seed.getSeed();
        }
        return result;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param group the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Group group) {
        //Base the compare score on the evaluative score.
        double evalThis = this.getResult().getEvaluationValue();
        double evalO = group.getResult().getEvaluationValue();
        if(evalThis == evalO) {
            return 0;
        } else if(evalThis > evalO) {
            return -1;
        } else {
            return 1;
        }
    }
}

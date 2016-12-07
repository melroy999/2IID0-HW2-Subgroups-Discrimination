package instance.search;

import instance.Instance;
import instance.Type;
import instance.attribute.AbstractAttribute;
import instance.result.EvaluationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class SubGroup implements Comparable<SubGroup> {
    private final AbstractAttribute attribute;
    private final String value;
    private EvaluationResult heuristic;
    private final SubGroup subGroup;

    public SubGroup(AbstractAttribute attribute, String value) {
        this.attribute = attribute;
        this.value = value;
        this.subGroup = null;
    }


    public SubGroup(AbstractAttribute attribute, String value, SubGroup subGroup) {
        this.attribute = attribute;
        this.value = value;
        this.subGroup = subGroup;
    }

    public AbstractAttribute getAttribute() {
        return attribute;
    }

    public String getValue() {
        return value;
    }

    public boolean isPartOfSubgroup(Instance instance) {
        return !instance.getValue(attribute).contains("?") && attribute.isPartOfSubgroup(this, instance.getValue(attribute)) && (subGroup == null || subGroup.isPartOfSubgroup(instance));
    }

    public SubGroup getSubGroup() {
        return subGroup;
    }

    public EvaluationResult getHeuristic() {
        return heuristic;
    }

    public double getEvaluation() {
        return heuristic.getEvaluation();
    }

    public void setEvaluation(EvaluationResult evaluation) {
        this.heuristic = evaluation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubGroup)) return false;

        SubGroup subGroup1 = (SubGroup) o;

        if (attribute != null ? !attribute.equals(subGroup1.attribute) : subGroup1.attribute != null) return false;
        if (value != null ? !value.equals(subGroup1.value) : subGroup1.value != null) return false;
        if (heuristic != null ? !heuristic.equals(subGroup1.heuristic) : subGroup1.heuristic != null) return false;
        return subGroup != null ? subGroup.equals(subGroup1.subGroup) : subGroup1.subGroup == null;

    }

    public boolean recursiveHasAttribute(AbstractAttribute abstractAttribute) {
        boolean result = /*this.value.equals(value) &&*/ this.attribute == abstractAttribute;
        if(subGroup != null) {
            result = result || subGroup.recursiveHasAttribute(abstractAttribute);
        }
        return result;
    }

    public boolean recursiveHasAllSubgroups(SubGroup sg2, boolean allowSubgroupWithDifferentValues) {
        SubGroup sg1 = this;
        HashSet<String> thisAttributes = new HashSet<>();
        thisAttributes.add(sg1.getAttribute().getName() + (allowSubgroupWithDifferentValues ? "" : sg1.getValue()));
        while(sg1.getSubGroup() != null) {
            sg1 = sg1.getSubGroup();
            thisAttributes.add(sg1.getAttribute().getName() + (allowSubgroupWithDifferentValues ? "" : sg1.getValue()));
        }

        HashSet<String> subGroupAttributes = new HashSet<>();
        subGroupAttributes.add(sg2.getAttribute().getName() + (allowSubgroupWithDifferentValues ? "" : sg2.getValue()));
        while(sg2.getSubGroup() != null) {
            sg2 = sg2.getSubGroup();
            subGroupAttributes.add(sg2.getAttribute().getName() + (allowSubgroupWithDifferentValues ? "" : sg2.getValue()));
        }

        return thisAttributes.equals(subGroupAttributes);
    }

    @Override
    public int hashCode() {
        int result = attribute != null ? attribute.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (heuristic != null ? heuristic.hashCode() : 0);
        result = 31 * result + (subGroup != null ? subGroup.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SubGroup{" +
                "attribute=" + attribute +
                ", value='" + (attribute.getType() == Type.NUMERIC ? "<=" : "") + value + '\'' +
                ", heuristic=" + heuristic +
                ", subGroup=" + subGroup +
                '}';
    }

    public String shortNotation() {
        return attribute.getName() + ((attribute.getType() == Type.NUMERIC ? " <= " : " = ") + value) + (subGroup != null ? " \u2227 " + subGroup.shortNotation() : "");
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
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(SubGroup o) {
        double evalThis = this.getHeuristic().getEvaluation();
        double evalO = o.getHeuristic().getEvaluation();
        if(evalThis - evalO == 0) {
            return 0;
        } else if(evalThis > evalO) {
            return -1;
        } else {
            return 1;
        }
    }
}

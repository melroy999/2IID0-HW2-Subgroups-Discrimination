package instance.search;

import instance.Instance;
import instance.attribute.AbstractAttribute;

public class SubGroup {
    private final AbstractAttribute attribute;
    private final String value;
    private int evaluation;

    public SubGroup(AbstractAttribute attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public AbstractAttribute getAttribute() {
        return attribute;
    }

    public String getValue() {
        return value;
    }

    public boolean isPartOfSubgroup(Instance instance) {
        return !instance.getValue(attribute).contains("?") && attribute.isPartOfSubgroup(this, instance.getValue(attribute));
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubGroup subGroup = (SubGroup) o;

        if (attribute != null ? !attribute.equals(subGroup.attribute) : subGroup.attribute != null) return false;
        return value != null ? value.equals(subGroup.value) : subGroup.value == null;

    }

    @Override
    public int hashCode() {
        int result = attribute != null ? attribute.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SubGroup{" +
                "attribute=" + attribute +
                ", value='" + value + '\'' +
                ", evaluation=" + evaluation +
                '}';
    }
}

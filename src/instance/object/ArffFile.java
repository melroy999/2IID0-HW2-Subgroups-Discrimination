package instance.object;

import instance.attribute.AbstractAttribute;

import java.util.List;

/**
 * Object representing the complete dataset.
 */
public class ArffFile {
    //values representing the data within an arff file.
    private final AbstractAttribute[] attributes;
    private final Instance[] instances;
    private final String relation;
    private final int target;

    /**
     * Create the arff dataset.
     *
     * @param attributeList The list of attributes in the dataset.
     * @param instanceList The list of instances in the dataset.
     * @param relation The name of the relation.
     * @param target The id of the target attribute.
     */
    public ArffFile(List<AbstractAttribute> attributeList, List<Instance> instanceList, String relation, int target) {
        this.attributes = new AbstractAttribute[attributeList.size()];
        for(int i = 0; i < attributeList.size(); i++) {
            this.attributes[i] = attributeList.get(i);
        }

        this.instances = new Instance[instanceList.size()];
        for(int i = 0; i < instanceList.size(); i++) {
            this.instances[i] = instanceList.get(i);
        }

        //Set the target attribute index.
        this.target = target;

        //Set the dataset name.
        this.relation = relation;
    }

    /**
     * Get the list of attributes.
     *
     * @return The list of attributes.
     */
    public AbstractAttribute[] getAttributes() {
        return attributes;
    }

    /**
     * Get the list of instances.
     *
     * @return The list of instances.
     */
    public Instance[] getInstances() {
        return instances;
    }

    /**
     * Get the name of the dataset.
     *
     * @return The name of the dataset.
     */
    public String getRelation() {
        return relation;
    }

    /**
     * Index of the target attribute.
     *
     * @return Index of the target attribute.
     */
    public int getTarget() {
        return target;
    }
}

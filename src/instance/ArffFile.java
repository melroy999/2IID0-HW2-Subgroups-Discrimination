package instance;

import instance.attribute.AbstractAttribute;

import java.util.HashMap;
import java.util.List;

public class ArffFile {
    private final AbstractAttribute[] attributes;
    private final Instance[] instances;
    private final String relation;

    public ArffFile(List<AbstractAttribute> attributeList, List<Instance> instanceList, String relation) {
        this.attributes = new AbstractAttribute[attributeList.size()];
        for(int i = 0; i < attributeList.size(); i++) {
            this.attributes[i] = attributeList.get(i);
        }

        this.instances = new Instance[instanceList.size()];
        for(int i = 0; i < instanceList.size(); i++) {
            this.instances[i] = instanceList.get(i);
        }

        this.relation = relation;
    }

    public AbstractAttribute[] getAttributes() {
        return attributes;
    }

    public Instance[] getInstances() {
        return instances;
    }

    public String getRelation() {
        return relation;
    }
}

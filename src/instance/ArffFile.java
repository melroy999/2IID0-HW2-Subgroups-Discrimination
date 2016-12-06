package instance;

import instance.attribute.AbstractAttribute;

import java.util.HashMap;
import java.util.List;

public class ArffFile {
    private final AbstractAttribute[] attributes;
    private final Instance[] instances;
    private final String relation;
    private final HashMap<AbstractAttribute, String[]> attributeInstanceValues = new HashMap<>();

    public ArffFile(List<AbstractAttribute> attributeList, List<Instance> instanceList, String relation) {
        this.attributes = new AbstractAttribute[attributeList.size()];
        for(int i = 0; i < attributeList.size(); i++) {
            this.attributes[i] = attributeList.get(i);
        }

        this.instances = new Instance[instanceList.size()];
        for(int i = 0; i < instanceList.size(); i++) {
            this.instances[i] = instanceList.get(i);
            for(AbstractAttribute attribute : attributes) {
                String[] mapping = attributeInstanceValues.getOrDefault(attribute, new String[instances.length]);
                mapping[i] = this.instances[i].getValue(attribute);
                attributeInstanceValues.put(attribute, mapping);
            }

        }

        this.relation = relation;
    }

    public AbstractAttribute[] getAttributes() {
        return attributes;
    }

    public Instance[] getInstances() {
        return instances;
    }

    public String[] getAttributeInstances(AbstractAttribute attribute) {
        return attributeInstanceValues.getOrDefault(attribute, new String[0]);
    }

    public String getRelation() {
        return relation;
    }
}

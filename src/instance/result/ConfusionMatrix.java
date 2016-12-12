package instance.result;

import instance.object.Group;
import instance.object.Instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfusionMatrix {
    //Values characterizing a heuristic result.
    public final double p;
    public final double n;
    public final double P;
    public final double N;
    public final double up;
    public final double un;

    //Contained instances, which includes unknown instances.
    private final List<Instance> containedInstances;

    private static final HashMap<Group, ConfusionMatrix> groupToConfusionMatrix = new HashMap<>();

    public static int getMemorylength() {
        return (groupToConfusionMatrix.size());
    }

    /**
     * Create a heuristic result containing the confusion matrix.
     *
     * @param p The p value in the confusion table.
     * @param P The P value in the confusion table.
     * @param n The n value in the confusion table.
     * @param N The N value in the confusion table.
     * @param up The amount of entries with unknown source that are positive in the confusion table.
     * @param un The amount of entries with unknown source that are negative in the confusion table.
     * @param containedInstances The instances that are contained within the group.
     */
    private ConfusionMatrix(double p, double P, double n, double N, double up, double un, List<Instance> containedInstances) {
        this.p = p;
        this.n = n;
        this.P = P;
        this.N = N;
        this.up = up;
        this.un = un;
        this.containedInstances = containedInstances;
    }

    /**
     * Get a heuristic result with the values of the confusion table set.
     *
     * @param group The group to use as cutoff.
     * @param instances The list of instances that the confusion table should be build from.
     * @param target The target value.
     * @return Heuristic result with the values of the confusion table set.
     */
    public static ConfusionMatrix getConfusionTable(Group group, List<Instance> instances, String target) {
        //Check whether we already know the result.
        ConfusionMatrix result = groupToConfusionMatrix.get(group);

        //Create the result if result is null.
        if(result == null) {
            double p = 0;
            double n = 0;
            double P = 0;
            double N = 0;
            double up = 0;
            double un = 0;

            List<Instance> containedInstances = new ArrayList<>();

            ConfusionMatrix seedResult = null;
            //Check whether we already have seen the seed, as we can iterate over the lists that are contained within the subgroup.
            if(group.getSeed() != null) {
                seedResult = groupToConfusionMatrix.get(group.getSeed());
                if(seedResult != null) {
                    instances = new ArrayList<>(seedResult.getContainedInstances());
                }
            }

            //Iterate over the list of instances, which might be shorter already because of the previous step.
            for(Instance instance : instances) {
                Group.ContainsHelper containsValue = group.containsInstance(instance);

                if (instance.getTargetValue().equals(target)) {
                    switch (containsValue) {
                        case UNKNOWN:
                            up++;
                            break;
                        case TRUE:
                            p++;
                            containedInstances.add(instance);
                            break;
                    }
                    P++;
                } else {
                    switch (containsValue) {
                        case UNKNOWN:
                            un++;
                            break;
                        case TRUE:
                            n++;
                            containedInstances.add(instance);
                            break;
                    }
                    N++;
                }
            }

            //Do not forget to add the P - p and N - n factors of the seed!
            if(seedResult != null) {
                //Also subtract the unknown value, as we still iterate over them!
                p += seedResult.P - seedResult.p - seedResult.up;
                n += seedResult.N - seedResult.n - seedResult.un;
            }

            //Create the result, and add it to the map.
            result = new ConfusionMatrix(p, P, n, N, up, un, containedInstances);
            groupToConfusionMatrix.put(group, result);
        } else {
            //System.out.println("Get successful");
        }

        return result;
    }

    public List<Instance> getContainedInstances() {
        return containedInstances;
    }

    @Override
    public String toString() {
        return "ConfusionMatrix{" +
                "p=" + p +
                ", n=" + n +
                ", P=" + P +
                ", N=" + N +
                ", up=" + up +
                ", un=" + un +
                '}';
    }
}

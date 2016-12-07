package instance.result;

public class EvaluationResult {
    private final double positive;
    private final double negative;
    private final double falsePositive;
    private final double falseNegative;
    private final double sum;
    private final int instancesInSubgroup;
    private double evaluation;

    public EvaluationResult(double positive, double negative, double falsePositive, double falseNegative, int instancesInSubgroup) {
        this.positive = positive;
        this.negative = negative;
        this.falsePositive = falsePositive;
        this.falseNegative = falseNegative;
        this.sum = positive + negative - falsePositive - falseNegative;
        this.instancesInSubgroup = instancesInSubgroup;
    }

    public double getPositive() {
        return positive;
    }

    public double getNegative() {
        return negative;
    }

    public double getFalsePositive() {
        return falsePositive;
    }

    public double getFalseNegative() {
        return falseNegative;
    }

    public double getSum() {
        return sum;
    }

    public int getInstancesInSubgroup() {
        return instancesInSubgroup;
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    public double positivePercentOfPositiveClass() {
        double positiveSpaceSize = getPositive() + getFalsePositive();
        positiveSpaceSize = positiveSpaceSize == 0 ? -1 : positiveSpaceSize;
        return getPositive() / positiveSpaceSize;
    }

    public double negativePercentOfNegativeClass() {
        double negativeSpaceSize = getNegative() + getFalseNegative();
        negativeSpaceSize = negativeSpaceSize == 0 ? -1 : negativeSpaceSize;
        return getNegative() / negativeSpaceSize;
    }

    @Override
    public String toString() {
        return "EvaluationResult{" +
                "positive=" + positive +
                ", negative=" + negative +
                ", falsePositive=" + falsePositive +
                ", falseNegative=" + falseNegative +
                ", sum=" + sum +
                ", instancesInSubgroup=" + instancesInSubgroup +
                ", %1class=" + positivePercentOfPositiveClass() +
                ", %0class=" + negativePercentOfNegativeClass() +
                '}';
    }
}

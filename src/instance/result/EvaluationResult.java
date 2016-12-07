package instance.result;

public class EvaluationResult {
    private final double coveredPositive;
    private final double coveredNegative;
    private final double notCoveredPositive;
    private final double notCoveredNegative;
    private final double coveredInstancesCount;
    private final double nonCoveredInstancesCount;
    private final double positiveCount;
    private final double negativeCount;
    private final double totalInstances;
    private double evaluation;

    public EvaluationResult(double coveredPositive, double notCoveredPositive, double coveredNegative, double notCoveredNegative) {
        this.coveredPositive = coveredPositive;
        this.notCoveredPositive = notCoveredPositive;
        this.coveredNegative = coveredNegative;
        this.notCoveredNegative = notCoveredNegative;
        this.coveredInstancesCount = coveredPositive + coveredNegative;
        this.nonCoveredInstancesCount = notCoveredPositive + notCoveredNegative;
        this.positiveCount = coveredPositive + notCoveredPositive;
        this.negativeCount = coveredNegative + notCoveredNegative;
        this.totalInstances = this.positiveCount + this.negativeCount;
    }

    public double getCoveredPositive() {
        return coveredPositive;
    }

    public double getCoveredNegative() {
        return coveredNegative;
    }

    public double getNotCoveredPositive() {
        return notCoveredPositive;
    }

    public double getNotCoveredNegative() {
        return notCoveredNegative;
    }

    public double getCoveredInstancesCount() {
        return coveredInstancesCount;
    }

    public double getNonCoveredInstancesCount() {
        return nonCoveredInstancesCount;
    }

    public double getPositiveCount() {
        return positiveCount;
    }

    public double getNegativeCount() {
        return negativeCount;
    }

    public double getTotalInstances() {
        return totalInstances;
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
    }

    @Override
    public String toString() {
        return "EvaluationResult{" +
                "p=" + coveredPositive +
                ", n=" + coveredNegative +
                ", P-p=" + notCoveredPositive +
                ", N-n=" + notCoveredNegative +
                ", p+n=" + coveredInstancesCount +
                ", N+P-p-n=" + nonCoveredInstancesCount +
                ", p+P=" + positiveCount +
                ", n+N=" + negativeCount +
                ", N+P=" + totalInstances +
                ", evaluation=" + evaluation +
                '}';
    }
}

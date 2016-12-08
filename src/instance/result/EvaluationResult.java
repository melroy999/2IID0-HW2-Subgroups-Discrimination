package instance.result;

public class EvaluationResult {
    private final double coveredPositive;
    private final double coveredNegative;
    private final double notCoveredPositive;
    private final double notCoveredNegative;
    private final double positiveCount;
    private final double negativeCount;
    private double evaluation;

    public EvaluationResult(double coveredPositive, double notCoveredPositive, double coveredNegative, double notCoveredNegative) {
        this.coveredPositive = coveredPositive;
        this.notCoveredPositive = notCoveredPositive;
        this.coveredNegative = coveredNegative;
        this.notCoveredNegative = notCoveredNegative;
        this.positiveCount = coveredPositive + notCoveredPositive;
        this.negativeCount = coveredNegative + notCoveredNegative;
    }

    public double getCoveredPositive() {
        return coveredPositive;
    }

    public double getCoveredNegative() {
        return coveredNegative;
    }

    public double getPositiveCount() {
        return positiveCount;
    }

    public double getNegativeCount() {
        return negativeCount;
    }

    public double getEvaluationValue() {
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
                ", p+n=" + (coveredPositive + coveredNegative) +
                ", N+P-p-n=" + (this.positiveCount + this.negativeCount - coveredPositive - coveredNegative) +
                ", P=" + positiveCount +
                ", N=" + negativeCount +
                ", N+P=" + (this.positiveCount + this.negativeCount) +
                ", evaluation=" + evaluation +
                '}';
    }
}

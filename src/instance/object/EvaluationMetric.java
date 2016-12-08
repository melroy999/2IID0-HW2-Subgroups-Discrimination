package instance.object;

/**
 * Evaluation metric used to have dynamic comparisons between instances, based on our needs.
 */
public enum EvaluationMetric {
    EQ {
        @Override
        public String toString() {
            return "==";
        }

        @Override
        public boolean compare(String left, String right) {
            return left.equals(right);
        }
    }, GTEQ {
        @Override
        public String toString() {
            return ">=";
        }

        @Override
        public boolean compare(String left, String right) {
            return Double.valueOf(left) >= Double.valueOf(right);
        }
    }, LTEQ {
        @Override
        public String toString() {
            return "<=";
        }

        @Override
        public boolean compare(String left, String right) {
            return Double.valueOf(left) <= Double.valueOf(right);
        }
    };

    /**
     * Get the string representation.
     * @return The evaluation metric in comparator form.
     */
    @Override
    public abstract String toString();

    /**
     * Compare the left hand side with the right hand side, which depends on the metric used.
     *
     * @param left The left hand value.
     * @param right The right hand value.
     * @return Whether we evaluate true or false.
     */
    public abstract boolean compare(String left, String right);

    /**
     * Get the evaluation metrics we should look at for a specific type of attribute.
     *
     * @param type the type we look at.
     * @return EQ, GTEQ, LTEQ if type is NUMERIC, EQ only otherwise.
     */
    public EvaluationMetric[] getValues(Type type) {
        if(type == Type.NUMERIC) {
            return new EvaluationMetric[]{EQ, GTEQ, LTEQ};
        } else {
            return new EvaluationMetric[]{EQ};
        }
    }
}

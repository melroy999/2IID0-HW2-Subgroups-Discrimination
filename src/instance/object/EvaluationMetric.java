package instance.object;

import java.util.HashMap;

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
            double leftValue, rightValue;
            if(valueMap.containsKey(left) && valueMap.containsKey(right)) {
                leftValue = valueMap.get(left);
                rightValue = valueMap.get(right);

                return leftValue == rightValue;
            } else {
                return left.equals(right);
            }
        }

        /**
         * Compare the left hand side with the right hand side, which depends on the metric used. Uses a strict comparison.
         *
         * @param left  The left hand value.
         * @param right The right hand value.
         * @return Whether we evaluate true or false.
         */
        @Override
        public boolean compareStrict(String left, String right) {
            return compare(left, right);
        }
    }, GTEQ {
        @Override
        public String toString() {
            return ">=";
        }

        @Override
        public boolean compare(String left, String right) {
            double leftValue, rightValue;
            if(!valueMap.containsKey(left)) {
                valueMap.put(left, Double.parseDouble(left));
            }
            if(!valueMap.containsKey(right)) {
                valueMap.put(right, Double.parseDouble(right));
            }

            leftValue = valueMap.get(left);
            rightValue = valueMap.get(right);

            return leftValue >= rightValue;
        }

        @Override
        public boolean compareStrict(String left, String right) {
            double leftValue, rightValue;
            if(!valueMap.containsKey(left)) {
                valueMap.put(left, Double.parseDouble(left));
            }
            if(!valueMap.containsKey(right)) {
                valueMap.put(right, Double.parseDouble(right));
            }

            leftValue = valueMap.get(left);
            rightValue = valueMap.get(right);

            return leftValue > rightValue;
        }
    }, LTEQ {
        @Override
        public String toString() {
            return "<=";
        }

        @Override
        public boolean compare(String left, String right) {
            double leftValue, rightValue;
            if(!valueMap.containsKey(left)) {
                valueMap.put(left, Double.parseDouble(left));
            }
            if(!valueMap.containsKey(right)) {
                valueMap.put(right, Double.parseDouble(right));
            }

            leftValue = valueMap.get(left);
            rightValue = valueMap.get(right);

            return leftValue <= rightValue;
        }

        @Override
        public boolean compareStrict(String left, String right) {
            double leftValue, rightValue;
            if(!valueMap.containsKey(left)) {
                valueMap.put(left, Double.parseDouble(left));
            }
            if(!valueMap.containsKey(right)) {
                valueMap.put(right, Double.parseDouble(right));
            }

            leftValue = valueMap.get(left);
            rightValue = valueMap.get(right);

            return leftValue < rightValue;
        }
    };

    private static HashMap<String, Double> valueMap = new HashMap<>();

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
     * Compare the left hand side with the right hand side, which depends on the metric used. Uses a strict comparison.
     *
     * @param left The left hand value.
     * @param right The right hand value.
     * @return Whether we evaluate true or false.
     */
    public abstract boolean compareStrict(String left, String right);

    /**
     * Get the evaluation metrics we should look at for a specific type of attribute.
     *
     * @param type the type we look at.
     * @return EQ, GTEQ, LTEQ if type is NUMERIC, EQ only otherwise.
     */
    public static EvaluationMetric[] getValues(Type type) {
        if(type == Type.NUMERIC) {
            return new EvaluationMetric[]{LTEQ, GTEQ};
        } else {
            return new EvaluationMetric[]{EQ};
        }
    }
}

package com.vityarthi.academic.strategy;

import com.vityarthi.academic.model.Grade;

/**
 * Concrete implementation of GradingStrategy following Indian Technical University / VIT 10-point scale:
 * 90-100: S  (10.0)
 * 80-89 : A  (9.0)
 * 70-79 : B  (8.0)
 * 60-69 : C  (7.0)
 * 55-59 : D  (6.0)
 * 50-54 : E  (5.0)
 * < 50  : F  (0.0)
 */
public class HonorsGradingStrategy implements GradingStrategy {

    @Override
    public Grade evaluate(double score) {
        if (score < 0.0 || score > 100.0) {
            throw new IllegalArgumentException("Score must be between 0.0 and 100.0");
        }

        if (score >= 90.0) {
            return new Grade(score, "S", 10.0);
        } else if (score >= 80.0) {
            return new Grade(score, "A", 9.0);
        } else if (score >= 70.0) {
            return new Grade(score, "B", 8.0);
        } else if (score >= 60.0) {
            return new Grade(score, "C", 7.0);
        } else if (score >= 55.0) {
            return new Grade(score, "D", 6.0);
        } else if (score >= 50.0) {
            return new Grade(score, "E", 5.0);
        } else {
            return new Grade(score, "F", 0.0);
        }
    }

    @Override
    public String getStrategyName() {
        return "VIT 10-Point Honors Scale";
    }
}

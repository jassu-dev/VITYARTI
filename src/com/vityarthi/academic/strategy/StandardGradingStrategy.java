package com.vityarthi.academic.strategy;

import com.vityarthi.academic.model.Grade;

/**
 * Concrete implementation of GradingStrategy following standard university grading scales:
 * 90-100: S/A+ (10.0 / 4.0)
 * 80-89 : A    (9.0 / 3.7)
 * 70-79 : B    (8.0 / 3.0)
 * 60-69 : C    (7.0 / 2.5)
 * 50-59 : D    (6.0 / 2.0)
 * < 50  : F    (0.0 / 0.0)
 */
public class StandardGradingStrategy implements GradingStrategy {

    @Override
    public Grade evaluate(double score) {
        if (score < 0.0 || score > 100.0) {
            throw new IllegalArgumentException("Score must be between 0.0 and 100.0");
        }

        if (score >= 90.0) {
            return new Grade(score, "A+", 4.0);
        } else if (score >= 80.0) {
            return new Grade(score, "A", 3.7);
        } else if (score >= 70.0) {
            return new Grade(score, "B", 3.0);
        } else if (score >= 60.0) {
            return new Grade(score, "C", 2.5);
        } else if (score >= 50.0) {
            return new Grade(score, "D", 2.0);
        } else {
            return new Grade(score, "F", 0.0);
        }
    }

    @Override
    public String getStrategyName() {
        return "Standard University 4.0 Scale";
    }
}

package com.vityarthi.academic.strategy;

import com.vityarthi.academic.model.Grade;

/**
 * Strategy Pattern Interface for Grade Calculations.
 * Demonstrates OOP Polymorphism and open-closed architectural design.
 */
public interface GradingStrategy {
    /**
     * Compute a Grade object containing letter grade and grade points for a numeric score.
     *
     * @param score Numerical percentage (0.0 to 100.0)
     * @return Grade value object
     */
    Grade evaluate(double score);

    /**
     * Returns a human-readable title for the grading scheme.
     */
    String getStrategyName();
}

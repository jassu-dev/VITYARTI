package com.vityarthi.academic.model;

import java.io.Serializable;

/**
 * Value object representing an academic grade with letter and point mappings.
 */
public class Grade implements Serializable {
    private static final long serialVersionUID = 1L;

    private final double numericalScore;
    private final String letterGrade;
    private final double gradePoint;

    public Grade(double numericalScore, String letterGrade, double gradePoint) {
        this.numericalScore = numericalScore;
        this.letterGrade = letterGrade;
        this.gradePoint = gradePoint;
    }

    public double getNumericalScore() {
        return numericalScore;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public boolean isPassing() {
        return gradePoint >= 2.0 && !"F".equalsIgnoreCase(letterGrade);
    }

    @Override
    public String toString() {
        return String.format("%.1f%% [%s] (GP: %.1f)", numericalScore, letterGrade, gradePoint);
    }
}

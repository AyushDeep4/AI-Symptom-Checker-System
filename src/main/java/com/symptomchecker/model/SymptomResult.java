package com.symptomchecker.model;

/**
 * Represents the result of symptom matching for a condition
 */
public class SymptomResult {
    private String conditionName;
    private int matchedCount;
    private int totalSymptoms;
    private int percentMatch;
    private String advice;

    public SymptomResult(String conditionName, int matchedCount, int totalSymptoms, int percentMatch, String advice) {
        this.conditionName = conditionName;
        this.matchedCount = matchedCount;
        this.totalSymptoms = totalSymptoms;
        this.percentMatch = percentMatch;
        this.advice = advice;
    }

    public String getConditionName() {
        return conditionName;
    }

    public int getMatchedCount() {
        return matchedCount;
    }

    public int getTotalSymptoms() {
        return totalSymptoms;
    }

    public int getPercentMatch() {
        return percentMatch;
    }

    public String getAdvice() {
        return advice;
    }
}


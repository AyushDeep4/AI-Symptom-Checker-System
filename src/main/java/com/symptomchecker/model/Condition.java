package com.symptomchecker.model;

import java.util.List;

/**
 * Represents a medical condition with its associated symptoms
 */
public class Condition {
    private String name;
    private List<String> symptoms;
    private String advice;

    public Condition(String name, List<String> symptoms, String advice) {
        this.name = name;
        this.symptoms = symptoms;
        this.advice = advice;
    }

    public String getName() {
        return name;
    }

    public List<String> getSymptoms() {
        return symptoms;
    }

    public String getAdvice() {
        return advice;
    }
}


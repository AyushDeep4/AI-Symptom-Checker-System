package com.symptomchecker.service;

import com.symptomchecker.model.Condition;
import com.symptomchecker.model.SymptomResult;
import com.symptomchecker.repository.ConditionRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class that handles symptom checking logic
 */
public class SymptomCheckerService {
    private static final List<String> DEFAULT_SYMPTOMS = Arrays.asList(
        "Fever", "Cough", "Headache", "Sore Throat", "Body Aches",
        "Fatigue", "Runny Nose", "Sneezing", "Nausea", "Stomach Pain",
        "Dizziness", "Chest Pain", "Shortness of Breath", "Loss of Appetite", "Chills"
    );

    private List<Condition> conditions;

    public SymptomCheckerService() {
        initializeConditions();
    }

    /**
     * Initialize the condition data with symptoms and advice, preferring database values.
     */
    private void initializeConditions() {
        ConditionRepository repository = new ConditionRepository();
        try {
            List<Condition> loadedConditions = repository.loadConditions();
            if (loadedConditions != null && !loadedConditions.isEmpty()) {
                conditions = loadedConditions;
                return;
            } else {
                System.err.println("No conditions found in the database. Falling back to defaults.");
            }
        } catch (IllegalStateException ex) {
            System.err.println("Failed to load conditions from the database. Falling back to defaults.");
            System.err.println(ex.getMessage());
        }

        conditions = createDefaultConditions();
    }

    private List<Condition> createDefaultConditions() {
        List<Condition> defaults = new ArrayList<>();
        defaults.add(new Condition("Common Cold",
            List.of("Cough", "Runny Nose", "Sore Throat", "Sneezing"),
            "Rest, drink fluids, and consider warm soups."));

        defaults.add(new Condition("Flu",
            List.of("Fever", "Body Aches", "Fatigue", "Headache", "Chills"),
            "Rest well, stay hydrated, and monitor fever."));

        defaults.add(new Condition("Food Poisoning",
            List.of("Nausea", "Stomach Pain", "Dizziness"),
            "Hydrate with small sips; avoid heavy foods."));

        defaults.add(new Condition("Seasonal Allergies",
            List.of("Sneezing", "Runny Nose", "Cough"),
            "Stay indoors during high pollen; consider over-the-counter meds."));

        defaults.add(new Condition("Migraine",
            List.of("Headache", "Nausea", "Dizziness"),
            "Rest in a quiet dark room, hydrate, and consider pain relief."));

        defaults.add(new Condition("Respiratory Infection",
            List.of("Cough", "Chest Pain", "Shortness of Breath", "Fever"),
            "Rest, fluids, and see a doctor if symptoms worsen."));

        return defaults;
    }

    /**
     * Check symptoms against all conditions and return matching results
     *
     * @param selectedSymptoms List of selected symptoms
     * @return List of SymptomResult sorted by match quality
     */
    public List<SymptomResult> checkSymptoms(List<String> selectedSymptoms) {
        if (selectedSymptoms == null || selectedSymptoms.isEmpty()) {
            return new ArrayList<>();
        }

        List<SymptomResult> results = new ArrayList<>();

        // Calculate matches for each condition
        for (Condition condition : conditions) {
            List<String> conditionSymptoms = condition.getSymptoms();
            if (conditionSymptoms == null || conditionSymptoms.isEmpty()) {
                continue;
            }

            int matchedCount = 0;
            for (String symptom : selectedSymptoms) {
                if (conditionSymptoms.contains(symptom)) {
                    matchedCount++;
                }
            }

            int percentMatch = Math.round((matchedCount * 100.0f) / conditionSymptoms.size());
            results.add(new SymptomResult(
                condition.getName(),
                matchedCount,
                conditionSymptoms.size(),
                percentMatch,
                condition.getAdvice()
            ));
        }

        // Filter: only conditions with 3+ matches
        List<SymptomResult> filtered = new ArrayList<>();
        for (SymptomResult result : results) {
            if (result.getMatchedCount() >= 3) {
                filtered.add(result);
            }
        }

        // Sort by matched count (desc), then percent (desc), then name (asc)
        filtered.sort((a, b) -> {
            if (b.getMatchedCount() != a.getMatchedCount()) {
                return Integer.compare(b.getMatchedCount(), a.getMatchedCount());
            }
            if (b.getPercentMatch() != a.getPercentMatch()) {
                return Integer.compare(b.getPercentMatch(), a.getPercentMatch());
            }
            return a.getConditionName().compareTo(b.getConditionName());
        });

        // Take top 3
        List<SymptomResult> topResults = new ArrayList<>();
        for (int i = 0; i < Math.min(3, filtered.size()); i++) {
            topResults.add(filtered.get(i));
        }

        // If no 3+ matches, show top 3 closest (matched >= 1) as fallback
        if (topResults.isEmpty()) {
            List<SymptomResult> nonZero = new ArrayList<>();
            for (SymptomResult result : results) {
                if (result.getMatchedCount() > 0) {
                    nonZero.add(result);
                }
            }

            nonZero.sort((a, b) -> {
                if (b.getMatchedCount() != a.getMatchedCount()) {
                    return Integer.compare(b.getMatchedCount(), a.getMatchedCount());
                }
                if (b.getPercentMatch() != a.getPercentMatch()) {
                    return Integer.compare(b.getPercentMatch(), a.getPercentMatch());
                }
                return a.getConditionName().compareTo(b.getConditionName());
            });

            for (int i = 0; i < Math.min(3, nonZero.size()); i++) {
                topResults.add(nonZero.get(i));
            }
        }

        return topResults;
    }

    /**
     * Get all available symptoms
     */
    public List<String> getAllSymptoms() {
        Set<String> symptoms = new LinkedHashSet<>(DEFAULT_SYMPTOMS);
        for (Condition condition : conditions) {
            if (condition.getSymptoms() != null) {
                symptoms.addAll(condition.getSymptoms());
            }
        }
        return new ArrayList<>(symptoms);
    }
}